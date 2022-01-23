package com.bolsadeideas.springboot.reactor.app;

import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor.app.models.Usuario;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ejemploFlux1();
		
		ejemploFlux2();
		
		inmutabilidadObservables();
	}

	private void ejemploFlux1() {
		// Creación de un observable
		Flux<Usuario> nombres = Flux
				.just("Juan", "María", "Perico", "Mariano", "Justino", "Zoilo")
				.doOnNext(e -> {
			if (e.isEmpty()) {
				throw new RuntimeException("La lista no puede estar vacía");
			}
			System.out.println(e);
		}).map(nombre -> nombre.toUpperCase()).map(nombre -> {
			Usuario us = new Usuario(nombre, "Apellido desconocido");
			System.out.println(us);
			return us;
		});

//				nombres.subscribe(e -> log.info(e), error -> log.error(error.getMessage()));

		nombres.subscribe(e -> log.info(e.getNombre()), error -> log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				log.info("Ha finalizado la ejecución de ejemploFlux1");
			}
		});
	}
	
	private void ejemploFlux2() {
		Flux<Usuario> nombres2 = Flux
				.just("Pedro Piqueras", "Antonio Salinas", "Perico Zubizarreta", "Melisa Bermúdez", "Dario Souto")
				.map(nombre -> new Usuario(nombre.split(" ")[0], nombre.split(" ")[1]))
				.filter(usuario -> "Bermúdez".equalsIgnoreCase(usuario.getApellidos()))
				.doOnNext(System.out::println);
		
		nombres2.subscribe(e -> log.info(e.getNombre()), error -> log.error(error.getMessage()), new Runnable() {
			
			@Override
			public void run() {
				log.info("Ha finalizado la ejecución de ejemploFlux2");
			}
		});
	}
	
	private void inmutabilidadObservables() {
		Flux<String> nombres3 = Flux
				.just("Juan", "María", "Perico", "Mariano", "Justino", "Zoilo");
		
		nombres3.filter(nombre -> "Juan".equalsIgnoreCase(nombre));
		
		nombres3.subscribe(e -> log.info(e));
		
	}
	
	private void createObservableFromList() {
		
	}

}
