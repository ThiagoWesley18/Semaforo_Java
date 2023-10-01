package barbearia;

import java.time.Instant;

public class Cabo {
	int tempo;
	int categoria;
	Instant startTimeCabo;
	
	public Cabo(int categoria, int tempo) {
		this.tempo = tempo;
		this.categoria = categoria;
	}
	
	public Cabo(int categoria, int tempo, Instant startTimeCabo) {
		this.tempo = tempo;
		this.categoria = categoria;
		this.startTimeCabo = startTimeCabo;
	}
}
