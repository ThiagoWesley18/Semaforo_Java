package barbearia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class ThreadFila extends Thread{
	private static  LinkedList<Oficial> oficial = new LinkedList<>();
	private static LinkedList<Sargento> sargento = new LinkedList<>();
	private static LinkedList<Cabo> cabo = new LinkedList<>();
	private Semaphore fila;
	private int categoria;
	private int tempo;
	
	public ThreadFila(Semaphore fila) {
		this(0,0,fila);
	}
	
	public ThreadFila(int categoria, int tempo, Semaphore fila) {
		this.categoria = categoria;
		this.tempo = tempo;
		this.fila = fila;
		this.start();
	}
	
	public int getTamTotal() {
		return getTamOficial() + getTamSargento() + getTamCabo();
	}
	public synchronized int getTamOficial() {
		return oficial.size();
	}
	public synchronized int getTamSargento() {
		return sargento.size();
	}
	public synchronized int getTamCabo() {
		return cabo.size();
	}
	
	public ArrayList<Integer> getTempoAtendimentoCabo() {
		ArrayList<Integer> tempos = new ArrayList<>();
		
		if(getTamCabo() != 0) {
			for(Cabo i : cabo) {
				tempos.add(i.tempo);
			}
		}else
			tempos.add(0);
		return tempos;
	}
	public ArrayList<Integer> getTempoAtendimentoOficial() {
		ArrayList<Integer> tempos = new ArrayList<>();
		
		if(getTamOficial() != 0) {
			for(Oficial i : oficial) {
				tempos.add(i.tempo);
			}
		}else
			tempos.add(0);
		return tempos;
	}
	public ArrayList<Integer> getTempoAtendimentoSargento() {
		ArrayList<Integer> tempos = new ArrayList<>();
		
		if(getTamSargento() != 0) {
			for(Sargento i : sargento) {
				tempos.add(i.tempo);
			}
		}else
			tempos.add(0);
		return tempos;
	}
	
	public void run() {
		if( (getTamOficial() + getTamSargento() + getTamCabo()) < 20 ) {
			try {
				// espera liberar permissões para usar o barbeiro de acordo com o numero
				// de permissões especificada no BarbeariaMain.
				fila.acquire();
				// usa o barbeiro de acordo com o tempo de corte.
				switch(categoria){
					case 1 : 
						cortaOficial();
						if(getTamOficial() != 0) {
							oficial.removeLast();
							System.out.println("oficial terminou o corte.");
						}
					
					case 2 : 
						cortaSargento();
						if(getTamSargento() != 0) {
							sargento.removeLast();
							System.out.println("Sargento terminou o corte.");
						}
						
					case 3 : 
						cortaCabo();
						if(getTamCabo() != 0) {
							cabo.removeLast();
							System.out.println("Cabo terminou o corte.");
						}
				}
				
			} catch (InterruptedException e) {
				System.out.println("Erro no semaforo");
				e.printStackTrace();
			}finally {
				// libera o barbeiro do corte
				fila.release();
			}
		}
		
	}
	
	private void cortaOficial() {
		try {
			System.out.println("Corte do Oficial");
			oficial.addLast(new Oficial(categoria, tempo));
			Thread.sleep(1000 * tempo);
		} catch (InterruptedException e) {
			System.out.println("Erro no corte de cabelo do oficial!");
			e.printStackTrace();
		}
	}
	private void cortaSargento() {
		try {
			System.out.println("Corte do Sargento");
			sargento.addLast(new Sargento(categoria, tempo));
			Thread.sleep(1000 * tempo);
		} catch (InterruptedException e) {
			System.out.println("Erro no corte de cabelo do Sargento!");
			e.printStackTrace();
		}
	}
	private void cortaCabo() {
		try {
			System.out.println("Corte do Cabo");
			cabo.addLast(new Cabo(categoria, tempo));
			Thread.sleep(1000 * tempo);
		} catch (InterruptedException e) {
			System.out.println("Erro no corte de cabelo do Cabo!");
			e.printStackTrace();
		}
	}
	
}
