package barbearia;

public class TempoAleatorio {
	
	public static int setTempo(int categoria) {
		int tempo = 0;
		if(categoria == 0) {
			tempo = 0;
		}else if(categoria == 1) {
			tempo = (int)(Math.random() * ((6 - 4) + 1)) + 4;
		}else if(categoria == 2) {
			tempo = (int)(Math.random() * ((4 - 2) + 1)) + 2;
		}else if(categoria == 3) {
			tempo = (int)(Math.random() * ((3 - 1) + 1)) + 1;
		}
		
		return tempo;
	}
}
