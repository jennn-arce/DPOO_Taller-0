package uniandes.dpoo.taller0.procesamiento;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uniandes.dpoo.taller0.modelo.Atleta;
import uniandes.dpoo.taller0.modelo.Evento;
import uniandes.dpoo.taller0.modelo.Genero;
import uniandes.dpoo.taller0.modelo.Pais;

/**
 * Esta es la clase que es capaz de calcular estadísticas sobre los juegos
 * olímpicos. Para calcular las estadísticas, esta clase agrupa la información
 * sobre los atletas, países y eventos, pero no tiene información sobre las
 * participaciones (eso es responsabilidad de los atletas y de los eventos).
 */
public class CalculadoraEstadisticas
{
	// ************************************************************************
	// Atributos
	// ************************************************************************

	/**
	 * Una lista con todos los atletas. En esta lista los atletas no están
	 * repetidos.
	 */
	private List<Atleta> atletas;

	/**
	 * Una lista con todos los países para los cuales hay al menos un atleta.
	 */
	private List<Pais> paises;

	/**
	 * Una lista con los eventos registrados. En esta lista puede aparecer dos veces
	 * el mismo deporte pero sólo si corresponde a años diferentes.
	 */
	private List<Evento> eventos;

	// ************************************************************************
	// Constructores
	// ************************************************************************

	/**
	 * Construye una calculadora de estadísticas, guardando la información
	 * proporcionada sobre atletas, países y eventos.
	 * 
	 * @param atletas Un mapa con los atletas, donde las llaves son los nombres de
	 *                los atletas y los valores son los atletas.
	 * @param paises  Un mapa con los países, donde las llaves son los nombres de
	 *                los países y los valores son los países.
	 * @param eventos Una lista con los eventos.
	 */
	public CalculadoraEstadisticas(Map<String, Atleta> atletas, Map<String, Pais> paises, List<Evento> eventos)
	{
		this.atletas = new ArrayList<Atleta>(atletas.values());
		this.paises = new ArrayList<Pais>(paises.values());
		this.eventos = eventos;
	}

	// ************************************************************************
	// Métodos
	// ************************************************************************

	/**
	 * Calcula cuáles fueron los atletas que participaron en cada evento para el año
	 * indicado
	 * 
	 * @param anio En año que se quiere consultar
	 * @return Un mapa donde las llaves son los nombres de los eventos y los valores
	 *         son los atletas que participaron en cada evento
	 */
	public Map<String, List<Atleta>> atletasPorAnio(int anio)
	{
		Map<String, List<Atleta>> resultado = new HashMap<String, List<Atleta>>();

		for (Evento unEvento : eventos)
		{
			if (unEvento.darAnio() == anio)
			{
				List<Atleta> atletasEnEvento = unEvento.darAtletasEnEvento();
				resultado.put(unEvento.darDeporte(), atletasEnEvento);
			}
		}

		return resultado;
	}

	/**
	 * Calcula cuáles fueron las medallas que ganó un atleta en un rango de años
	 * 
	 * @param anioInicial  El año inicial para el rango
	 * @param anioFinal    El año final para el rango
	 * @param nombreAtleta El nombre del atleta
	 * @return Una lista con la información de las medallas que ganó el atleta. Cada
	 *         registro es un mapa con tres llaves: "evento", que tiene asociado el
	 *         nombre del evento en el que el atleta ganó una medalla; "anio", que
	 *         tiene asociado el año en el que se llevó a cabo el evento; y
	 *         "medalla", que tiene asociado el tipo de medalla que ganó el atleta
	 *         ("gold", "silver" o "bronze").
	 * 
	 *         Si el nombre no corresponde al de ningún atleta, retorna null.
	 */
	public List<Map<String, Object>> medallasEnRango(int anioInicial, int anioFinal, String nombreAtleta)
	{
		List<Map<String, Object>> resultado = new ArrayList<Map<String, Object>>();
		Atleta elAtleta = buscarAtleta(nombreAtleta);
		if (elAtleta != null)
		{
			resultado = elAtleta.contarMedallasEnRango(anioInicial, anioFinal);
		}

		return resultado;
	}

	/**
	 * Compila la información de los atletas de un país.
	 * 
	 * @param nombrePais El nombre del país de interés.
	 * @return Una lista de mapas con la información de todos los atletas del país.
	 *         Cada registro de un atleta queda en un mapa que tiene tres llaves:
	 *         "evento", que tiene asociado el nombre del evento en el que participó
	 *         el atleta; "anio", que tiene asociado el año en el que el atleta
	 *         participó en el evento; y "nombre" que tiene asociado el nombre del
	 *         atleta.
	 * 
	 *         Si no se encuentra el país con el nombre indicado, se retorna null.
	 */
	public List<Map<String, Object>> atletasPorPais(String nombrePais)
	{
		List<Map<String, Object>> resultado = null;
		Pais elPais = buscarPais(nombrePais);
		if (elPais != null)
		{
			resultado = new ArrayList<Map<String, Object>>();
			resultado = elPais.consultarAtletas();
		}
		return resultado;
	}

	/**
	 * Calcula cuál es el país con más medallistas en los juegos olímpicos. Si hay
	 * más de un país con la mayor cantidad de medallistas, los encuentra a todos.
	 * 
	 * Este método se basa en la cantidad de medallistas diferentes (atletas que han
	 * ganado medallas) y no en la cantidad de medallas.
	 * 
	 * @return Un mapa que tiene la información de los países con más medallistas.
	 *         Las llaves en el mapa son los nombres de los países. Los valores son
	 *         la cantidad de medallistas. Si hay sólo un país que sea el que más
	 *         medallas tenga, el mapa tiene sólo una llave.
	 */
	public Map<String, Integer> paisConMasMedallistas()
	{
		Map<String, Integer> resultado = new HashMap<String, Integer>();

		int mayorCantidadMedallistas = -1;

		for (Pais unPais : paises)
		{
			int cantidadMedallistasPais = unPais.contarMedallistas();

			if (cantidadMedallistasPais >= mayorCantidadMedallistas)
			{
				if (cantidadMedallistasPais > mayorCantidadMedallistas)
				{
					resultado.clear();
					mayorCantidadMedallistas = cantidadMedallistasPais;
				}
				resultado.put(unPais.darNombre(), mayorCantidadMedallistas);
			}
		}

		return resultado;
	}

	/**
	 * Consulta cuáles son los atletas que han ganado al menos una medalla en el
	 * evento indicado, en cualquier año.
	 * 
	 * @param nombreEvento El nombre del evento de interés.
	 * @return Una lista con los atletas que han ganado al menos una medalla en el
	 *         evento. Si ningún atleta ha ganado una medalla en el evento o si el
	 *         nombre no corresponde el de ningún evento, retorna una lista vacía.
	 */
	public List<Atleta> medallistasPorEvento(String nombreEvento)
	{
		Set<Atleta> medallistas = new HashSet<Atleta>();

		for (Evento evento : eventos)
		{
			if (evento.darDeporte().equals(nombreEvento))
			{
				List<Atleta> medallistasEvento = evento.darMedallistas();
				medallistas.addAll(medallistasEvento);
			}
		}
		return new ArrayList<>(medallistas);
	}

	/**
	 * Calcula cuáles son los atletas que han ganado más medallas que la cantidad
	 * mínima indicada
	 * 
	 * @param cantidadMinimaMedallas La cantidad de medallas que se quiere usar para
	 *                               filtrar los atletas.
	 * @return Un mapa donde aparece la información de todos los atletas que han
	 *         ganado más que la cantidad mínima de medallas indicada. En este mapa
	 *         las llaves son los nombres de los atletas y los valores son las
	 *         cantidades de medallas ganadas por el atleta correspondiente.
	 */
	public Map<String, Integer> atletasConMasMedallas(int cantidadMinimaMedallas)
	{
		Map<String, Integer> medallistas = new HashMap<>();
		for (Atleta atleta : atletas)
		{
			int cantidadMedallas = atleta.contarMedallas();
			if (cantidadMedallas > cantidadMinimaMedallas)
				medallistas.put(atleta.darNombre(), cantidadMedallas);
		}

		return medallistas;
	}

	/**
	 * Calcula cuál o cuáles son los atletas estrella, es decir los que más medallas
	 * hayan ganado (independientemente del tipo de medalla).
	 * 
	 * Si hay un atleta que ha ganado más medallas que todos los demás, el resultado
	 * sólo tiene un elemento. De lo contrario aparecen todos los atletas que estén
	 * empatados en el primer lugar.
	 * 
	 * @return Un diccionario donde aparecen los atletas estrella. Por cada atleta
	 *         hay una entrada en el mapa donde la llave es el nombre del atleta y
	 *         el valor es la cantidad de medallas que ganó.
	 */
	public Map<String, Integer> atletaEstrella()
	{
		Map<String, Integer> estrellas = new HashMap<>();
		int mayorCantidad = 0;

		for (Atleta atleta : atletas)
		{
			int cantidadMedallas = atleta.contarMedallas();
			if (cantidadMedallas >= mayorCantidad)
			{
				if (cantidadMedallas > mayorCantidad)
				{
					estrellas.clear();
					mayorCantidad = cantidadMedallas;
				}
				estrellas.put(atleta.darNombre(), cantidadMedallas);
			}
		}

		return estrellas;
	}

	/**
	 * Calcula cuál ha sido el país con el mejor desempeño en el evento.
	 * 
	 * El mejor desempeño se calcula con base en la cantidad de medallas ganadas y
	 * su tipo. Es decir, que el mejor país es aquel que tenga más medallas de oro,
	 * en caso de empate con otro país, será mejor el que tenga más medallas de
	 * plata entre estos, y si el empate persiste, se definirá por el número de
	 * medallas de bronce.
	 * 
	 * Si el empate persiste, en el resultado aparecerá más de un país.
	 * 
	 * @param nombreEvento El nombre del evento de interés.
	 * @return Un mapa donde las llaves son nombres de países y los valores son
	 *         arreglos con tres enteros: la cantidad de medallas de oro, la
	 *         cantidad de medallas de plata y la cantidad de medallas de bronce.
	 */
	public Map<String, int[]> mejorPaisEvento(String nombreEvento)
	{
		int[] mejorResultado = { -1, -1, -1 };
		Map<String, int[]> resultado = new HashMap<>();

		for (Pais pais : paises)
		{
			int[] medallasPais = pais.calcularMedallasEvento(nombreEvento);

			if (medallasPais[0] > mejorResultado[0])
			{
				mejorResultado = medallasPais;
				resultado.clear();
				resultado.put(pais.darNombre(), medallasPais);
			}
			else if (medallasPais[0] == mejorResultado[0])
			{
				if (medallasPais[1] > mejorResultado[1])
				{
					mejorResultado = medallasPais;
					resultado.clear();
					resultado.put(pais.darNombre(), medallasPais);
				}
				else if (medallasPais[1] == mejorResultado[1])
				{
					if (medallasPais[2] > mejorResultado[2])
					{
						mejorResultado = medallasPais;
						resultado.clear();
						resultado.put(pais.darNombre(), medallasPais);
					}
					else if (medallasPais[2] == mejorResultado[2])
					{
						resultado.put(pais.darNombre(), medallasPais);
					}
				}
			}

		}

		return resultado;
	}

	/**
	 * Consulta cuál es el atleta que ha participado en más deportes diferentes.
	 * 
	 * Si un atleta ha participado en el mismo deporte en años diferentes, sólo se
	 * cuenta una vez.
	 * 
	 * Si hay más de un atleta empatado por el primer lugar, retorna el primero
	 * alfabéticamente de acuerdo al nombre.
	 * 
	 * @return El Atleta que ha participado en más deportes diferentes.
	 */
	public Atleta buscarAtletaTodoterreno()
	{
		Atleta todoterreno = null;
		int mayorCantidadDeportes = -1;

		for (Atleta unAtleta : atletas)
		{
			int cantidadDeportes = unAtleta.contarDeportes();
			if (cantidadDeportes > mayorCantidadDeportes || (cantidadDeportes == mayorCantidadDeportes
					&& unAtleta.darNombre().compareTo(todoterreno.darNombre()) > 0))
			{
				todoterreno = unAtleta;
				mayorCantidadDeportes = cantidadDeportes;
			}
		}

		return todoterreno;
	}

	/**
	 * Consulta cuáles han sido los medallistas de un determinado país y de un
	 * determinado género.
	 * 
	 * @param nombrePais   El nombre del país de interés.
	 * @param generoAtleta El género de interés.
	 * @return Retorna un mapa donde las llaves son los nombres de los atletas del
	 *         país y del género que han sido medallistas y los valores son una
	 *         lista con información de sus medallas. La información de cada medalla
	 *         también es un mapa que tiene tres llaves: "evento", que tiene
	 *         asociado el nombre del evento; "anio", que tiene asociado el año en
	 *         el que el atleta ganó la medalla; y "medalla" que tiene asociado el
	 *         tipo de medalla.
	 */
	public Map<String, List<Map<String, Object>>> medallistasPorNacionGenero(String nombrePais, Genero generoAtleta)
	{
		Map<String, List<Map<String, Object>>> resultado = null;
		Pais elPais = buscarPais(nombrePais);
		if (elPais != null)
		{
			resultado = elPais.consultarMedallistasGenero(generoAtleta);
		}
		return resultado;
	}

	/**
	 * Calcula qué porcentaje de los atletas ha sido medallista (ha ganado al menos
	 * una medalla).
	 * 
	 * @return Un número entre 0 y 1 que indica el porcentaje de atletas que ha sido
	 *         medallista
	 */
	public double porcentajeMedallistas()
	{
		double cantidadAtletas = atletas.size();
		double cantidadMedallistas = 0;
		for (Atleta unAtleta : atletas)
		{
			if (unAtleta.esMedallista())
				cantidadMedallistas++;
		}
		return cantidadMedallistas / cantidadAtletas;
	}

	/**
	 * Retorna el país con el nombre indicado
	 * 
	 * @param nombrePais El nombre del país que se está buscando
	 * @return El país con el nombre dado o null si no se encuentra.
	 */
	private Pais buscarPais(String nombrePais)
	{
		Pais elPais = null;
		for (int i = 0; i < paises.size() && elPais == null; i++)
		{
			if (paises.get(i).darNombre().equals(nombrePais))
				elPais = paises.get(i);
		}
		return elPais;
	}

	/**
	 * Retorna el atleta con el nombre indicado
	 * 
	 * @param nombreAtleta El nombre del atleta que se está buscando
	 * @return El atleta con el nombre dado o null si no se encuentra.
	 */
	private Atleta buscarAtleta(String nombreAtleta)
	{
		Atleta elAtleta = null;
		for (int i = 0; i < atletas.size() && elAtleta == null; i++)
		{
			if (atletas.get(i).darNombre().equals(nombreAtleta))
				elAtleta = atletas.get(i);
		}
		return elAtleta;
	}

	/**
	 * Retorna una colección con los nombres de los eventos
	 * 
	 * @return Colección con los nombres de los eventos, sin repetir
	 */
	public Collection<String> darNombresDeportes()
	{
		Collection<String> nombres = new HashSet<String>();
		for (Evento evento : eventos)
		{
			nombres.add(evento.darDeporte());
		}

		return nombres;
	}


	/**
	 * MODIFICACI�N TALLER 0
	 * 
	 * Se busca el pa�s de un atleta en espec�fico
	 *  @param nombreAtleta El nombre del atleta que se est� buscando
	 *  @return String que representa el pa�s de la atleta
	 */

	public String paisAtleta(String nombreAtleta)
	{
		Atleta elAtleta = buscarAtleta(nombreAtleta);
		String paisAtleta = "NA"; 
		if (elAtleta != null)
		{
			paisAtleta = (elAtleta.darPais().darNombre());
		}
		return paisAtleta;
	}

}


