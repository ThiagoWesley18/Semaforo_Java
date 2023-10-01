package barbearia;

public class GeraRelatorio {
	private ThreadFila thread;
	
	public GeraRelatorio(ThreadFila thread) {
		this.thread = thread;
	}
	
	public Integer getComprimentoFilaOficial() {
		return thread.getTamOficial();
	}
	public Integer getComprimentoFilaSargento() {
		return thread.getTamSargento();
	}
	public Integer getComprimentoFilaCabo() {
		return thread.getTamCabo();
	}
	
	public Integer getTempoAtendimentoOficial() {
		Integer soma = 0;
		for(Integer i : thread.getTempoAtendimentoOficial())
			soma += i;
		return soma;
	}
	public Integer getTempoAtendimentoSargento() {
		Integer soma = 0;
		for(Integer i : thread.getTempoAtendimentoSargento())
			soma += i;
		return soma;
	}
	public Integer getTempoAtendimentoCabo() {
		Integer soma = 0;
		for(Integer i : thread.getTempoAtendimentoCabo())
			soma += i;
		return soma;
	}
}
