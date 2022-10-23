import java.util.ArrayList;
import java.util.Scanner;

public class Main {
  
  static java.util.Random generator = new java.util.Random();
  public static char [][] tableroDisparos;
  public static int suma;
  public static int[][] mapaDensidad;
  public static int[][] mapaDensidadTotal;
  public static int[][] mapaDensidadTarget;
  public static boolean portaAviones = true; 
  public static boolean buque = true;        
  public static boolean submarino = true;    
  public static boolean crucero = true;      
  public static boolean destructor = true;   
  public static int vidaPortaAviones = 5;
  public static int vidaBuque = 4;
  public static int vidaSubmarino = 3;
  public static int vidaCrucero = 3;
  public static int vidaDestructor = 2;
  public static ArrayList<Integer> coordenadasX = new ArrayList<Integer>();
  public static ArrayList<Integer> coordenadasY = new ArrayList<Integer>(); 

  public static void main(String[] args) {

    long startTime = System.currentTimeMillis();
    //Pedir numero de oceanos por teclado
    Scanner sc = new Scanner(System.in);
    System.out.println("Ingrese el numero de oceanos: ");
    int oceanos = sc.nextInt();
    sc.close();

    int n = 10;
    for (int juegos = 0; juegos < oceanos; juegos++) {

      //if (juegos % 5000 == 0) {System.out.println("Oceanos " + juegos);} 

      Tablero tablero = new Tablero(n);
      tableroDisparos = new char[n+2][n+2];
      mapaDensidad = new int[n+2][n+2];
      mapaDensidadTotal = new int[n+2][n+2];
      mapaDensidadTarget = new int[n+2][n+2];

      llenarMatriz(tableroDisparos, n);

      while (tablero.ganar() == 0) {

        //INICIO HUNT MODE

        rellenarMatriz(mapaDensidadTotal, n);
        obtenerMapaDensidadTotal(n);

        //PARIDAD
        for (int i = 1; i < n+1; i++) {
          for (int j = 1; j < n+1; j++) {
            //Paridad de 2
            if(destructor == true){
              if ((i%2 == 0 && j%2 == 0) || (i%2 != 0 && j%2 != 0)) {
                mapaDensidadTotal[i][j] = 0;
              } 
            }
            //Paridad de 3
            if(destructor == false && (crucero == true || submarino == true)){
              if ((i+j == 4 || i+j == 7 || i+j == 10 || i+j == 13 || i+j == 16 || i+j == 19)) {
                mapaDensidadTotal[i][j] = 0;
              } 
            }
            //Paridad de 4
            if(destructor == false && crucero == false && submarino == false && buque == true){
              if (i+j == 5 || i+j == 9 || i+j == 13 || i+j == 17) {
                mapaDensidadTotal[i][j] = 0;
              } 
            }
            //Paridad de 5
            if(destructor == false && crucero == false && submarino == false && buque == false && portaAviones == true){
              if (i+j == 6 || i+j == 11) {
                mapaDensidadTotal[i][j] = 0;
              } 
            }
          }        
        }
        
        int mayorValor = obtenerNumeroMayor(mapaDensidadTotal, n);

        ArrayList<int[]> posiciones = new ArrayList<int[]>();
        for(int fila=1;fila<mapaDensidadTotal[0].length-1;fila++){
          for(int columna=1;columna<mapaDensidadTotal.length-1;columna++){
            if(mapaDensidadTotal[fila][columna] == mayorValor){
              int[] posicion = new int[2];
              posicion[0] = fila;
              posicion[1] = columna;
              posiciones.add(posicion);
            }
          }
        }
        int[] posicion = posiciones.get(generator.nextInt(posiciones.size()));
        int fila = posicion[0];
        int columna = posicion[1];
        while (tableroDisparos[fila][columna] != '0'){
          posicion = posiciones.get(generator.nextInt(posiciones.size()));
          fila = posicion[0];
          columna = posicion[1];
        }
        char disparado = tablero.disparo(fila,columna);
        actualizarMatriz(tableroDisparos, fila, columna, disparado);
        //FINAL DEL HUNT MODE
        
        if(disparado != '0' && disparado != 'X'){

          coordenadasX.add(fila);
          coordenadasY.add(columna);
          if(disparado == 'A') vidaPortaAviones--;
          if(disparado == 'B') vidaBuque--;
          if(disparado == 'S') vidaSubmarino--;
          if(disparado == 'C') vidaCrucero--;
          if(disparado == 'D') vidaDestructor--;
          
          boolean barcoDestruido = false;

          if(vidaPortaAviones == 0 && portaAviones == true) {portaAviones = false; barcoDestruido = true;}
          if(vidaBuque == 0 && buque == true) {buque = false; barcoDestruido = true;} 
          if(vidaSubmarino == 0 && submarino == true) {submarino = false; barcoDestruido = true;}
          if(vidaCrucero == 0 && crucero == true) {crucero = false; barcoDestruido = true;}
          if(vidaDestructor == 0 && destructor == true) {destructor = false; barcoDestruido = true;}

          //INICIO TARGET MODE
          while(barcoDestruido == false){

            
            rellenarMatriz(mapaDensidadTarget, n);
            for(int i=0;i<coordenadasX.size();i++){
              obtenerMapaDensidadObjetivoTotal(n, coordenadasX.get(i), coordenadasY.get(i));               
            }

            int mayorValorCaza = obtenerNumeroMayor(mapaDensidadTarget, n);
            ArrayList<int[]> posicionesCaza = new ArrayList<int[]>();
            for(int i=1;i<mapaDensidadTarget[0].length-1;i++){
              for(int j=1;j<mapaDensidadTarget.length-1;j++){
                if(mapaDensidadTarget[i][j] == mayorValorCaza){
                  int[] posicionCaza = new int[2];
                  posicionCaza[0] = i;
                  posicionCaza[1] = j;
                  posicionesCaza.add(posicionCaza);
                }                 
              }
            }

            int[] posicionCaza = posicionesCaza.get(generator.nextInt(posicionesCaza.size()));
            int filaCaza = posicionCaza[0];
            int columnaCaza = posicionCaza[1];

            char disparadoCaza = tablero.disparo(filaCaza,columnaCaza);
            actualizarMatriz(tableroDisparos, filaCaza, columnaCaza, disparadoCaza);

            if(disparadoCaza != '0' && disparadoCaza != 'X'){
              coordenadasX.add(filaCaza);
              coordenadasY.add(columnaCaza);
            }

            if(disparadoCaza == 'A') {vidaPortaAviones--;}
            if(disparadoCaza == 'B') {vidaBuque--;}
            if(disparadoCaza == 'S') {vidaSubmarino--;}
            if(disparadoCaza == 'C') {vidaCrucero--;}
            if(disparadoCaza == 'D') {vidaDestructor--;}

            if(vidaPortaAviones == 0 && portaAviones == true) {portaAviones = false; barcoDestruido = true;}
            if(vidaBuque == 0 && buque == true) {buque = false; barcoDestruido = true;} 
            if(vidaSubmarino == 0 && submarino == true) {submarino = false; barcoDestruido = true;}
            if(vidaCrucero == 0 && crucero == true) {crucero = false; barcoDestruido = true;}
            if(vidaDestructor == 0 && destructor == true) {destructor = false; barcoDestruido = true;}
        
          }
          //Resetear disparos del target
          coordenadasX.clear();
          coordenadasY.clear();
          //FINAL TARGET MODE
        } 
        
        if (tablero.ganar() != 0) {
          suma = suma + tablero.ganar();;
        }
      }
      
      //Resetear variables para el siguiente oceano
      portaAviones = true;
      buque = true;
      submarino = true;
      crucero = true;
      destructor = true;
      vidaPortaAviones = 5;
      vidaBuque = 4;
      vidaSubmarino = 3;
      vidaCrucero = 3;
      vidaDestructor = 2;
    } 
    System.out.println("Promedio de disparos: " + suma/oceanos);
    long endTime = System.currentTimeMillis();     
    long totalTime = endTime - startTime;
    System.out.println("Tiempo de ejecucion: " + totalTime/1000 + " segundos");
  }
  //FIN DEL MAIN

  //Imprimir en consola una matriz char de nxn
  public static void imprimirMatriz(char [][] matriz, int n) {
    for (int i = 1; i < n+1; i++) {      
      for (int j = 1; j < n+1; j++) {
        System.out.print(matriz[i][j] + " ");
      }
      System.out.println("");
    }
  }
  
  //Imprimir en consola una matriz int de nxn
  public static void imprimeMatriz(int [][] matriz, int n) {
    for (int i = 1; i < n+1; i++) {
      for (int j = 1; j < n+1; j++) {
        System.out.print(matriz[i][j] + " ");
      }
      System.out.println("");
    }
  }

  //Rellenar matriz int con 0
  public static void rellenarMatriz(int [][] matriz, int n) {
    for (int i = 1; i < n+1; i++) {
      for (int j = 1; j < n+1; j++) {
        matriz[i][j] = 0;
      }
    }
  }

  //Rellenar matriz char con '0'
  public static void llenarMatriz(char [][] matriz, int n) {
    for (int i = 1; i < n+1; i++) {
      for (int j = 1; j < n+1; j++) {
        matriz[i][j] = '0';
      }
    }
  }

  //Actualiza la matriz donde se ha disparado (miss con X y hit con la letra del barco)
  public static char [][] actualizarMatriz(char [][] matriz, int x, int y, char disparado) {
    if (disparado == '0') {
        tableroDisparos[x][y] = 'X';
    } else {
      tableroDisparos[x][y] = disparado;
    }
    return matriz;
  }

  //Sumar dos matrices
  public static int [][] sumarMatrices(int [][] matriz1, int [][] matriz2, int n){
    int [][] matriz3 = new int [n+2][n+2];
    for(int fila=1;fila<matriz1[0].length-1;fila++){
      for(int columna=1;columna<matriz1.length-1;columna++){
        matriz3[fila][columna] = matriz1[fila][columna] + matriz2[fila][columna];
      }
    }
    return matriz3;
  }

  //Obtener el numero mayor de una matriz
  public static int obtenerNumeroMayor(int [][] matriz, int n){
    int mayor = 0;
    for(int fila=1;fila<matriz[0].length-1;fila++){
      for(int columna=1;columna<matriz.length-1;columna++){
        if(matriz[fila][columna]>mayor){
          mayor = matriz[fila][columna];
        }
      }
    }
    return mayor;
  }
 
  //Obtener matriz de funcion de densidad probabilistica para cuando se esta buscando un hit en el tablero (HUNT MODE)
  public static void obtenerMapaDensidadTotal(int n){

    if(destructor == true){
      mapaDensidad = densidadBarcos(tableroDisparos, n, mapaDensidad, 2, 'D');
      mapaDensidadTotal = sumarMatrices(mapaDensidad, mapaDensidadTotal, n); 
    }  
    if(submarino == true){
      mapaDensidad = densidadBarcos(tableroDisparos, n, mapaDensidad, 3, 'S');
      mapaDensidadTotal = sumarMatrices(mapaDensidad, mapaDensidadTotal, n); 
    }
    if(crucero == true){
      mapaDensidad = densidadBarcos(tableroDisparos, n, mapaDensidad, 3, 'C');
      mapaDensidadTotal = sumarMatrices(mapaDensidad, mapaDensidadTotal, n); 
    }
    if(buque == true){
      mapaDensidad = densidadBarcos(tableroDisparos, n, mapaDensidad, 4, 'B');
      mapaDensidadTotal = sumarMatrices(mapaDensidad, mapaDensidadTotal, n); 
    }
    if(portaAviones == true){
      mapaDensidad = densidadBarcos(tableroDisparos, n, mapaDensidad, 5, 'A');
      mapaDensidadTotal = sumarMatrices(mapaDensidad, mapaDensidadTotal, n); 
    }
  }

  //Obtener matriz de funcion de densidad probabilistica para cuando se esta derribando un barco (TARGET MODE)
  public static void obtenerMapaDensidadObjetivoTotal(int n, int fila, int columna){

    if(portaAviones == true){
      mapaDensidad = obtenerMapaDensidadObjetivo(tableroDisparos, n, 5 , fila, columna, 'A');
      mapaDensidadTarget = sumarMatrices(mapaDensidadTarget, mapaDensidad, n);
    }
    if(buque == true){
      mapaDensidad = obtenerMapaDensidadObjetivo(tableroDisparos, n, 4 , fila, columna, 'B');
      mapaDensidadTarget = sumarMatrices(mapaDensidadTarget, mapaDensidad, n);
    }
    if(submarino == true){
      mapaDensidad = obtenerMapaDensidadObjetivo(tableroDisparos, n, 3 , fila, columna, 'S');
      mapaDensidadTarget = sumarMatrices(mapaDensidadTarget, mapaDensidad, n);
    }
    if(crucero == true){
      mapaDensidad = obtenerMapaDensidadObjetivo(tableroDisparos, n, 3 , fila, columna, 'C');
      mapaDensidadTarget = sumarMatrices(mapaDensidadTarget, mapaDensidad, n);
    }
    if(destructor == true){
      mapaDensidad = obtenerMapaDensidadObjetivo(tableroDisparos, n, 2 , fila, columna, 'D');
      mapaDensidadTarget = sumarMatrices(mapaDensidadTarget, mapaDensidad, n);
    }
  }

  //Funcion de densidad probabilistica para un tipo de barco en todo el tablero (TARGET MODE)
  public static int [][] densidadBarcos(char [][] matriz, int n, int [][] densidad, int barco, char letraBarco) {

    for(int fila=1;fila<matriz[0].length-1;fila++){      
        for(int columna=1;columna<matriz.length-1;columna++){         
            if(matriz[fila][columna]=='0'){
                int celdasLibresVertical = 1;
                int celdasLibresHorizontal = 1;
                int densidadCelda = 0;

                //Direccion Norte
                if(fila-1>0 && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco)) celdasLibresVertical++;
                if(fila-2>0 && (matriz[fila-2][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && barco > 2) celdasLibresVertical++;
                if(fila-3>0 && (matriz[fila-3][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && barco > 3) celdasLibresVertical++;
                if(fila-4>0 && (matriz[fila-4][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && barco > 4) celdasLibresVertical++;

                //Direccion Sur
                if(fila+1<n+1 && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco)) celdasLibresVertical++;
                if(fila+2<n+1 && (matriz[fila+2][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && barco > 2) celdasLibresVertical++;
                if(fila+3<n+1 && (matriz[fila+3][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && barco > 3) celdasLibresVertical++;
                if(fila+4<n+1 && (matriz[fila+4][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && barco > 4) celdasLibresVertical++;
                if(celdasLibresVertical < barco) celdasLibresVertical = barco-1; 

                //Direccion Oeste
                if(columna-1>0 && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco)) celdasLibresHorizontal++;
                if(columna-2>0 && (matriz[fila][columna-2]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && barco > 2) celdasLibresHorizontal++;
                if(columna-3>0 && (matriz[fila][columna-3]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && barco > 3) celdasLibresHorizontal++;
                if(columna-4>0 && (matriz[fila][columna-4]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && barco > 4) celdasLibresHorizontal++;

                //Direccion Este
                if(columna+1<n+1 && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco)) celdasLibresHorizontal++;
                if(columna+2<n+1 && (matriz[fila][columna+2]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && barco > 2) celdasLibresHorizontal++;
                if(columna+3<n+1 && (matriz[fila][columna+3]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && barco > 3) celdasLibresHorizontal++;
                if(columna+4<n+1 && (matriz[fila][columna+4]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && barco > 4) celdasLibresHorizontal++;
                if(celdasLibresHorizontal < barco) celdasLibresHorizontal = barco-1;

                densidadCelda = (celdasLibresVertical-(barco-1)) + (celdasLibresHorizontal-(barco-1));             
                densidad[fila][columna] = densidadCelda;                 
            } else{
                densidad[fila][columna] = 0;
            }
        }
    }    
    return densidad;
  }

  //Funcion de densidad probabilistica de un tipo de barco para la periferia de un hit (HUNT MODE)
  public static int [][] obtenerMapaDensidadObjetivo(char [][] matriz, int n, int largoBarco, int fila, int columna, char letraBarco){
    int [][] mapaDensidad = new int [n+2][n+2];

    rellenarMatriz(mapaDensidad, n);
    int densidad = 0;

    //Direccion Norte
    if(fila-1>0 && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco)) {densidad = largoBarco - 1; mapaDensidad[fila-1][columna] = densidad;}
    if(fila-2>0 && (matriz[fila-2][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && largoBarco > 2) {densidad = largoBarco - 2; mapaDensidad[fila-2][columna] = densidad;}
    if(fila-3>0 && (matriz[fila-3][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && largoBarco > 3) {densidad = largoBarco - 3; mapaDensidad[fila-3][columna] = densidad;}
    if(fila-4>0 && (matriz[fila-4][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && (matriz[fila-1][columna]=='0' || matriz[fila-1][columna]==letraBarco) && largoBarco > 4) {densidad = largoBarco - 4; mapaDensidad[fila-4][columna] = densidad;}

    //Direccion Sur
    if(fila+1<n+1 && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco)) {densidad = largoBarco - 1; mapaDensidad[fila+1][columna] = densidad;}
    if(fila+2<n+1 && (matriz[fila+2][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && largoBarco > 2) {densidad = largoBarco - 2; mapaDensidad[fila+2][columna] = densidad;}
    if(fila+3<n+1 && (matriz[fila+3][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && largoBarco > 3) {densidad = largoBarco - 3; mapaDensidad[fila+3][columna] = densidad;}
    if(fila+4<n+1 && (matriz[fila+4][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && (matriz[fila+1][columna]=='0' || matriz[fila+1][columna]==letraBarco) && largoBarco > 4) {densidad = largoBarco - 4; mapaDensidad[fila+4][columna] = densidad;}

    //Direccion Oeste
    if(columna-1>0 && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco)) {densidad = largoBarco - 1; mapaDensidad[fila][columna-1] = densidad;}
    if(columna-2>0 && (matriz[fila][columna-2]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && largoBarco > 2) {densidad = largoBarco - 2; mapaDensidad[fila][columna-2] = densidad;}
    if(columna-3>0 && (matriz[fila][columna-3]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && largoBarco > 3) {densidad = largoBarco - 3; mapaDensidad[fila][columna-3] = densidad;}
    if(columna-4>0 && (matriz[fila][columna-4]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && (matriz[fila][columna-1]=='0' || matriz[fila][columna-1]==letraBarco) && largoBarco > 4) {densidad = largoBarco - 4; mapaDensidad[fila][columna-4] = densidad;}

    //Direccion Este
    if(columna+1<n+1 && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco)) {densidad = largoBarco - 1; mapaDensidad[fila][columna+1] = densidad;}
    if(columna+2<n+1 && (matriz[fila][columna+2]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && largoBarco > 2) {densidad = largoBarco - 2; mapaDensidad[fila][columna+2] = densidad;}
    if(columna+3<n+1 && (matriz[fila][columna+3]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && largoBarco > 3) {densidad = largoBarco - 3; mapaDensidad[fila][columna+3] = densidad;}
    if(columna+4<n+1 && (matriz[fila][columna+4]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && (matriz[fila][columna+1]=='0' || matriz[fila][columna+1]==letraBarco) && largoBarco > 4) {densidad = largoBarco - 4; mapaDensidad[fila][columna+4] = densidad;}

    return mapaDensidad;
  }
}     