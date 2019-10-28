package br.com.codenation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import br.com.codenation.desafio.annotation.Desafio;
import br.com.codenation.desafio.app.MeuTimeInterface;
import br.com.codenation.desafio.exceptions.CapitaoNaoInformadoException;
import br.com.codenation.desafio.exceptions.IdentificadorUtilizadoException;
import br.com.codenation.desafio.exceptions.JogadorNaoEncontradoException;
import br.com.codenation.desafio.exceptions.TimeNaoEncontradoException;
import br.com.codenation.model.Jogador;
import br.com.codenation.model.Time;

public class DesafioMeuTimeApplication implements MeuTimeInterface {
	
	List<Time> listaTimes = new ArrayList<Time>();
	List<Jogador> listaJogadores = new ArrayList<Jogador>();

	@Desafio("incluirTime")
	public void incluirTime(Long id, String nome, LocalDate dataCriacao, String corUniformePrincipal, String corUniformeSecundario) {
		
		Time timeExistente = buscarTime( id );
		
		if( timeExistente != null ) {
			throw new IdentificadorUtilizadoException();
		}
		
		Time time = new Time( id, nome, dataCriacao, corUniformePrincipal, corUniformeSecundario, null );
		listaTimes.add( time );
	}

	@Desafio("incluirJogador")
	public void incluirJogador(Long id, Long idTime, String nome, LocalDate dataNascimento, Integer nivelHabilidade, BigDecimal salario) {
		
		Jogador jogadorExistente = buscarJogador( id );
		Time timeExistente = buscarTime( idTime );
		
		if( jogadorExistente != null ) {
			throw new IdentificadorUtilizadoException();
		}
		if( timeExistente == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		Jogador jogador = new Jogador( id, idTime, nome, dataNascimento, nivelHabilidade, salario );
		listaJogadores.add( jogador );
		
	}

	@Desafio("definirCapitao")
	public void definirCapitao( Long idJogador ) {
		
		Jogador jogadorExistente = buscarJogador( idJogador );
		
		if( jogadorExistente == null ) {
			throw new JogadorNaoEncontradoException();
		}
		
		Time time = buscarTime( jogadorExistente.getIdTime() );

		listaTimes.stream().filter( t -> t.getId().equals( time.getId() ) ).findFirst().orElse(null).setIdJogador( jogadorExistente.getId() );
	}

	@Desafio("buscarCapitaoDoTime")
	public Long buscarCapitaoDoTime(Long idTime) {

		Time time = buscarTime( idTime );
		
		if( time == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		if( time.getIdJogador() == null ) {
			throw new CapitaoNaoInformadoException();
		}
		
		return time.getIdJogador();
		
	}

	@Desafio("buscarNomeJogador")
	public String buscarNomeJogador(Long idJogador) {

		Jogador jogador = buscarJogador( idJogador );
		
		if( jogador == null ) {
			throw new JogadorNaoEncontradoException();
		}
		
		return jogador.getNome();
		
	}

	@Desafio("buscarNomeTime")
	public String buscarNomeTime(Long idTime) {

		Time time = buscarTime( idTime );
		
		if( time == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		return time.getNome();
		
	}

	@Desafio("buscarJogadoresDoTime")
	public List<Long> buscarJogadoresDoTime(Long idTime) {

		Time time = buscarTime( idTime );
		
		if( time == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		return buscarIdJogadoresDoTime( time.getId() );
	}

	@Desafio("buscarMelhorJogadorDoTime")
	public Long buscarMelhorJogadorDoTime(Long idTime) {

		Time time = buscarTime( idTime );
		
		if( time == null ) {
			throw new TimeNaoEncontradoException();
		}
		return buscarIdMelhorJogadorDoTime( time.getId() );

	}

	@Desafio("buscarJogadorMaisVelho")
	public Long buscarJogadorMaisVelho(Long idTime) {

		Time time = buscarTime( idTime );
		
		if( time == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		return buscarIdJogadorMaisVelho( time.getId() );
		
	}

	@Desafio("buscarTimes")
	public List<Long> buscarTimes() {

		return listaTimes.stream().sorted( Comparator.comparingLong( Time::getId )).map( Time::getId ).collect( Collectors.toList() );
	}

	@Desafio("buscarJogadorMaiorSalario")
	public Long buscarJogadorMaiorSalario(Long idTime) {

		Time time = buscarTime( idTime );
		
		if( time == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		return buscarIdJogadorMaiorSalario( time.getId() );
	}

	@Desafio("buscarSalarioDoJogador")
	public BigDecimal buscarSalarioDoJogador(Long idJogador) {

		Jogador jogador = buscarJogador( idJogador );
		
		if( jogador == null ) {
			throw new JogadorNaoEncontradoException();
		}
		
		return buscarSalarioJogador( jogador.getId() );
		
	}

	@Desafio("buscarTopJogadores")
	public List<Long> buscarTopJogadores(Integer top) {

		return listaJogadores.stream().sorted( Comparator.comparing( Jogador::getNivelHabilidade ).reversed())
				.map( Jogador::getId ).limit( top ).collect( Collectors.toList() );
		
	}

	@Desafio("buscarCorCamisaTimeDeFora")
	public String buscarCorCamisaTimeDeFora(Long idTimeDaCasa, Long idTimeDeFora) {

		Time timeCasa = buscarTime( idTimeDaCasa );
		Time timeFora = buscarTime( idTimeDeFora );
		
		if( timeCasa == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		if( timeFora == null ) {
			throw new TimeNaoEncontradoException();
		}
		
		if( timeCasa.getCorUniformePrincipal().equals( timeFora.getCorUniformePrincipal() ) ) {
			return timeFora.getCorUniformeSecundario();
		}
		
		
		return timeFora.getCorUniformePrincipal();
		
	}
	
	private Time buscarTime( Long id ) {	
		return listaTimes.stream().filter( time -> time.getId().equals( id )).findFirst().orElse(null);
	}
	
	private Jogador buscarJogador( Long id ) {
		return listaJogadores.stream().filter( jogador ->
			jogador.getId().equals( id )).findFirst().orElse(null);
	}
	
	private List<Long> buscarIdJogadoresDoTime( Long id ) {
		return listaJogadores.stream().filter(jogador ->
			jogador.getIdTime().equals( id )).map( Jogador::getId ).sorted().collect( Collectors.toList() );
	}
	
	private Long buscarIdMelhorJogadorDoTime( Long id ) {
		return listaJogadores.stream().filter(jogador ->
			jogador.getIdTime().equals( id )).max(Comparator.comparingInt( Jogador::getNivelHabilidade )).map( Jogador::getId ).get();
	}
	
	private Long buscarIdJogadorMaisVelho( Long id ) {
		return listaJogadores.stream().filter( jogador ->
			jogador.getIdTime().equals( id )).max( Comparator.comparing( Jogador::getDataNascimento ).reversed()).map( Jogador::getId ).get();
	}
	
	private Long buscarIdJogadorMaiorSalario( Long id ) {
		return listaJogadores.stream().filter( jogador ->
		    jogador.getIdTime().equals( id )).sorted( Comparator.comparing( Jogador::getId )).max( Comparator.comparing( Jogador::getSalario )).map( Jogador::getId ).get();
	}
	
	private BigDecimal buscarSalarioJogador( Long id ) {
		return listaJogadores.stream().filter( jogador -> jogador.getId().equals( id )).map( Jogador::getSalario ).findFirst().get();
	}

}
