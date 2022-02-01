package uniandes.dpoo.taller0.modificacion;

import java.io.IOException;

import uniandes.dpoo.taller0.procesamiento.CalculadoraEstadisticas;
import uniandes.dpoo.taller0.procesamiento.LoaderOlimpicos;

public class Modificacion {

	public static void main(String[] args)
	{
		System.out.println("Hola, mundo!");
		
		CalculadoraEstadisticas calc = null;
		try {
			calc = LoaderOlimpicos.cargarArchivo("./data/atletas.csv");
		} catch (IOException e) {
			// Al ver los errores escog� usar un try and catch
			e.printStackTrace();
		}
		System.out.println(calc.paisConMasMedallistas());
	}
}
