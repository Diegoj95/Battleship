public class Tablero{
  static java.util.Random generator = new java.util.Random();
  private char [][] t;
  private int intentos = 0;
  public Tablero(int n){
    if(n<10) n=10;
    t = new char[n+2][n+2];
    for(int i=1;i<n+1;i++)
      for(int j=1;j<n+1;j++)
      	t[i][j]= '0';
    while(!ubicarNave(5,'A'));
    //Imprimir();
    while(!ubicarNave(4,'B'));
    //Imprimir();
    while(!ubicarNave(3,'C'));
    //Imprimir();
    while(!ubicarNave(3,'S'));
    //Imprimir();
    while(!ubicarNave(2,'D'));
	intentos = 0;
  }
  public char disparo(int x, int y){
    intentos++;
    if(x<1 || x>t.length) return 0;
    if(y<1 || y>t.length) return 0;
    char aux = t[x][y];
    if(aux!= '0') t[x][y]='X';
    return aux;
  }
  private boolean ubicarNave(int l, char tipo){
    int x = 1+generator.nextInt(t.length-2);
    int y = 1+generator.nextInt(t.length-2);
	//System.out.println(x + " " + y);
    int dir = generator.nextInt(4);
    //if(t[x-1][y]!='0' || t[x+1][y]!='0' || t[x][y-1]!='0' || t[x][y+1]!='0') return false;
    if(dir==0 && x-(l-1)<1) return false;
    if(dir==1 && x+(l-1)>t.length-1) return false;
    if(dir==2 && y-(l-1)<1) return false;
    if(dir==3 && y+(l-1)>t.length-1) return false;
    for(int i=0;i<l;i++){
      if(dir==0 && (t[x-i][y]!='0' || t[x-i][y+1]!='0' || t[x-i][y-1]!='0')) return false;
      if(dir==1 && (t[x+i][y]!='0' || t[x+i][y+1]!='0' || t[x+i][y-1]!='0')) return false;
      if(dir==2 && (t[x][y-i]!='0' || t[x-1][y-i]!='0' || t[x+1][y-i]!='0')) return false;
      if(dir==3 && (t[x][y+i]!='0' || t[x-1][y+i]!='0' || t[x+1][y+i]!='0')) return false;
    }
    if(dir==0 && t[x-(l)][y]!='0') return false;
    if(dir==1 && t[x+(l)][y]!='0') return false;
    if(dir==2 && t[x][y-(l)]!='0') return false;
    if(dir==3 && t[x][y+(l)]!='0') return false;
    for(int i=0;i<l;i++){
      if(dir==0) t[x-i][y]=tipo;
      if(dir==1) t[x+i][y]=tipo;
      if(dir==2) t[x][y-i]=tipo;
      if(dir==3) t[x][y+i]=tipo;
    }
    return true;
  }
  public int ganar(){
    for(int i=1;i<t[0].length-1;i++)
      for(int j=1;j<t.length-1;j++)
        if(t[i][j]!='0' && t[i][j]!='X') return 0;
    //Imprimir();
    return intentos;
  }
  public void Imprimir(){
    for(int i=1;i<t[0].length-1;i++){
      for(int j=1;j<t.length-1;j++)
      	System.out.print(t[i][j] + " ");
      System.out.println("");
    }
  }
}