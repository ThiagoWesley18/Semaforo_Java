package barbearia;

import java.time.Instant;

public class Sargento {
	int tempo;
	int categoria;
	Instant startTimeSargento;
	
	public Sargento(int categoria, int tempo) {
		this.tempo = tempo;
		this.categoria = categoria;
	}
	
	public Sargento(int categoria, int tempo, Instant startTimeSargento) {
		this.tempo = tempo;
		this.categoria = categoria;
		this.startTimeSargento = startTimeSargento;
	}
}
