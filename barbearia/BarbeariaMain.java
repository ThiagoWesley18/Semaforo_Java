package barbearia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.time.Duration;
import java.time.Instant;
import barbearia.*;

public class BarbeariaMain extends Thread {
	private static final ArrayList<Integer> comprimentoFilaOficial = new ArrayList<>();
	private static final ArrayList<Integer> comprimentoFilaSargento = new ArrayList<>();
	private static final ArrayList<Integer> comprimentoFilaCabo = new ArrayList<>();
	private static final ArrayList<Integer> temposAtendimentoOficial = new ArrayList<>();
	private static final ArrayList<Integer> temposAtendimentoSargento = new ArrayList<>();
	private static final ArrayList<Integer> temposAtendimentoCabo = new ArrayList<>();
	private static final LinkedList<Oficial> filaOficial = new LinkedList<>();
	private static final LinkedList<Sargento> filaSargento = new LinkedList<>();
	private static final LinkedList<Cabo> filaCabo = new LinkedList<>();
	private static int numOficial = 0;
	private static int numSargento = 0;
	private static int numCabo = 0;
	private static int numZero = 0;
	private int categoria;
	private int tempo;
	ThreadFila fila;

	public BarbeariaMain(ThreadFila fila) {
		this.fila = fila;
		this.start();
	}

	public BarbeariaMain(int categoria, int tempo) {
		this.categoria = categoria;
		this.tempo = tempo;
		this.fila = null;
		this.start();
	}

	public void run() {
		if (fila == null) {

			switch (categoria) {
				case 0:
					numZero++;
					break;
				case 1:
					filaOficial.addLast(new Oficial(categoria, tempo, Instant.now()));
					numOficial++;
					break;
				case 2:
					filaSargento.addLast(new Sargento(categoria, tempo, Instant.now()));
					numSargento++;
					break;
				case 3:
					filaCabo.addLast(new Cabo(categoria, tempo, Instant.now()));
					numCabo++;
					break;
			}
		} else {
			try {
				// executa de forma paralela o relatorio a cada 3 segundos.
				Thread.sleep(3000);
				GeraRelatorio relatorio = new GeraRelatorio(fila);
				// pega os comprimentos da fila de atendimentos por categoria.
				comprimentoFilaOficial.add(relatorio.getComprimentoFilaOficial());
				comprimentoFilaSargento.add(relatorio.getComprimentoFilaSargento());
				comprimentoFilaCabo.add(relatorio.getComprimentoFilaCabo());
				// pega o tempo de atendimentos por categoria.
				temposAtendimentoOficial.add(relatorio.getTempoAtendimentoOficial());
				temposAtendimentoSargento.add(relatorio.getTempoAtendimentoSargento());
				temposAtendimentoCabo.add(relatorio.getTempoAtendimentoCabo());
			} catch (InterruptedException e) {
				System.out.println("Erro em gerar o relatorio neste instante...");
			}
		}
	}

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		int categoria;
		int tempo;
		int tentativa = 0;
		int randomNum;

		ArrayList<ThreadFila> filasThread = new ArrayList<>();
		ArrayList<BarbeariaMain> filasBarbeariaMain = new ArrayList<>();
		ArrayList<Long> temposEsperaOficial = new ArrayList<>();
		ArrayList<Long> temposEsperaSargento = new ArrayList<>();
		ArrayList<Long> temposEsperaCabo = new ArrayList<>();
		Integer somaComprimento = 0;
		int tamComprimento;
		int numAtendimentosOficial = 0;
		int numAtendimentosSargento = 0;
		int numAtendimentosCabo = 0;
		int numAtendimetosVazio = 0;
		int somaTempoAtendimentosOficial = 0;
		int somaTempoAtendimentosSargento = 0;
		int somaTempoAtendimentosCabo = 0;
		int somaTempoEsperaOficial = 0;
		int somaTempoEsperaSargento = 0;
		int somaTempoEsperaCabo = 0;

		// numero de pessoas por vez na fila(1 - 3).
		int permissao = 3;
		Semaphore fila = new Semaphore(permissao);

		while (true) {
			 //categoria = scan.nextInt();
			 //tempo = scan.nextInt();

			// entradas aleatorias entre categorias de 0 a 3, e tempos aleatorios de acordo
			// com a categoria escolhida.
			categoria = (int) (Math.random() * ((3) + 1));
			tempo = TempoAleatorio.setTempo(categoria);

			// insere na fila de espera de forma paralela.
			filasBarbeariaMain.add(new BarbeariaMain(categoria, tempo));

			try {

				// espera de 1 a 5 segundos para adicionar na fila das cadeiras de
				// corte(ThreadFila).
				randomNum = (int) (Math.random() * ((5 - 1) + 1)) + 1;
				Thread.sleep(1000L * randomNum);

				try {
					// inseri nas cadeiras de corte(ThreadFila) por ordem de patente.
					if (!filaOficial.isEmpty()) {
						tentativa = 0;
						Oficial oficial = filaOficial.removeFirst();
						Duration duracaoFilaEspera = Duration.between(oficial.startTimeOficial, Instant.now());
						temposEsperaOficial.add(duracaoFilaEspera.toSeconds());
						numAtendimentosOficial++;
						filasThread.add(new ThreadFila(oficial.categoria, oficial.tempo, fila));
					} else if (!filaSargento.isEmpty()) {
						tentativa = 0;
						Sargento sargento = filaSargento.removeFirst();
						Duration duracaoFilaEspera = Duration.between(sargento.startTimeSargento, Instant.now());
						temposEsperaSargento.add(duracaoFilaEspera.toSeconds());
						numAtendimentosSargento++;
						filasThread.add(new ThreadFila(sargento.categoria, sargento.tempo, fila));
					} else if (!filaCabo.isEmpty()) {
						tentativa = 0;
						Cabo cabo = filaCabo.removeFirst();
						Duration duracaoFilaEspera = Duration.between(cabo.startTimeCabo, Instant.now());
						temposEsperaCabo.add(duracaoFilaEspera.toSeconds());
						numAtendimentosCabo++;
						filasThread.add(new ThreadFila(cabo.categoria, cabo.tempo, fila));
					} else {
						// se houver 3 tentativas seguintes o programa encerra.
						tentativa++;
						numAtendimetosVazio++;
					}

				} catch (Exception e) {

					System.out.println("Erro na fila de espera...");
				}

			} catch (InterruptedException e) {

				System.out.println("Erro na adimissão para a fila de corte...");
			}

			// caso não tenha mais ninguem na fila de espera.
			// se houver 3 tentativas seguintes o programa encerra.
			if (tentativa == 3) {
				break;
			}
			// Gerar os dados do relatorio de forma paralela.
			filasBarbeariaMain.add(new BarbeariaMain(new ThreadFila(fila)));
		}

		for (ThreadFila i : filasThread) {
			try {
				i.join();
			} catch (InterruptedException e) {
				System.out.println("Erro no encerramento dos atendimentos...");

			}
		}
		for (BarbeariaMain i : filasBarbeariaMain) {
			try {
				i.join();
			} catch (InterruptedException e) {
				System.out.println("Erro no encerramento das filas das espera ou na geraçao de dados de relatorio ...");

			}
		}
		System.out.println("\n----Relatorio das Atividades da Barbearia----\n");
        for (Integer integer : comprimentoFilaOficial) {
            somaComprimento += integer;
        }
        for (Integer integer : comprimentoFilaSargento) {
            somaComprimento += integer;
        }
        for (Integer integer : comprimentoFilaCabo) {
            somaComprimento += integer;
        }

		tamComprimento = comprimentoFilaOficial.size() + comprimentoFilaSargento.size() + comprimentoFilaCabo.size();
		System.out.println("Comprimento Medio das filas: " + (somaComprimento / tamComprimento));// Na maior parte do
																									// tempo as filas de
																									// corte estao
																									// vazias por causa
																									// do numero de
																									// barbeiros para o
																									// atendimento.

        for (Integer integer : temposAtendimentoOficial) {
            somaTempoAtendimentosOficial += integer;
        }
        for (Integer integer : temposAtendimentoSargento) {
            somaTempoAtendimentosSargento += integer;
        }
        for (Integer integer : temposAtendimentoCabo) {
            somaTempoAtendimentosCabo += integer;
        }
		System.out.println("\nTempo Medio de Atendimento do Oficial: "
				+ (somaTempoAtendimentosOficial / temposAtendimentoOficial.size()));
		System.out.println("Tempo Medio de Atendimento do Sargento: "
				+ (somaTempoAtendimentosSargento / temposAtendimentoSargento.size()));
		System.out.println(
				"Tempo Medio de Atendimento do Cabo: " + (somaTempoAtendimentosCabo / temposAtendimentoCabo.size()));

        for (Long aLong : temposEsperaOficial) {
            somaTempoEsperaOficial += aLong;
        }
        for (Long aLong : temposEsperaSargento) {
            somaTempoEsperaSargento += aLong;
        }
        for (Long aLong : temposEsperaCabo) {
            somaTempoEsperaCabo += aLong;
        }
		System.out.println(
				"\nTempo Medio de Espera do Oficial: " + (somaTempoEsperaOficial / temposEsperaOficial.size()));
		System.out.println(
				"Tempo Medio de Espera do Sargento: " + (somaTempoEsperaSargento / temposEsperaSargento.size()));
		System.out.println("Tempo Medio de Espera do Cabo: " + (somaTempoEsperaCabo / temposEsperaCabo.size()));

		System.out.println("\nNumero de Atendimentos de Oficial: " + numAtendimentosOficial);
		System.out.println("Numero de Atendimentos de Sargento: " + numAtendimentosSargento);
		System.out.println("Numero de Atendimentos de Cabo: " + numAtendimentosCabo);
		System.out.println("Numero de Vezes que a fila de espera ficou vazia: " + numAtendimetosVazio);

		System.out.println("\nNumero Total de Oficial: " + numOficial);
		System.out.println("Numero Total de Sargento: " + numSargento);
		System.out.println("Numero Total de Cabo: " + numCabo);
		System.out.println("Numero Total de vazio: " + numZero);

		System.out.println("\n----Fim do Relatorio das Atividades da Barbearia----\n");
	}

}
