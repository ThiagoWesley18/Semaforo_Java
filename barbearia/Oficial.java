package barbearia;

import java.time.Instant;

public class Oficial {
	int tempo;
	int categoria;
	Instant startTimeOficial;
	
	public Oficial(int categoria, int tempo) {
		this.tempo = tempo;
		this.categoria = categoria;
	}
	
	public Oficial(int categoria, int tempo, Instant startTimeOficial) {
		this.tempo = tempo;
		this.categoria = categoria;
		this.startTimeOficial = startTimeOficial;
	}
}
