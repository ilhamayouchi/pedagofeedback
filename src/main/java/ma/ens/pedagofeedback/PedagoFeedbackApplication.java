package ma.ens.pedagofeedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class PedagoFeedbackApplication {

	public static void main(String[] args) {
		// Heure du Maroc, pour comparer l'heure des séances avec l'heure actuelle
		TimeZone.setDefault(TimeZone.getTimeZone("Africa/Casablanca"));
		SpringApplication.run(PedagoFeedbackApplication.class, args);
	}
}