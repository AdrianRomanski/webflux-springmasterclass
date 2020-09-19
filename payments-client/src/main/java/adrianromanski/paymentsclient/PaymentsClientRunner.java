package adrianromanski.paymentsclient;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Setter
@Component
@RequiredArgsConstructor
@ConfigurationProperties("payments")
public class PaymentsClientRunner implements CommandLineRunner {

    private String url;
    private String path;
    private final PaymentsRepository paymentsRepository;

    private Flux<Payment> getPayments() {
        return WebClient.builder()
                .baseUrl(url)
                .build()
                .get()
                .uri(path)
                .retrieve()
                .bodyToFlux(Payment.class);
    }

    @Override
    public void run(String... args) throws Exception {
        getPayments()
                .flatMap(paymentsRepository::save)
                .subscribe(System.out::println);
    }
}
