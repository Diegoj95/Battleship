# Battleship

Battleship es un juego donde hay barcos ubicados en una grilla bidimensional (oceano). El oceano es de dimensión 10x10, y existen 5 barcos ubicados ahí. Los barcos pueden ubicarse sólo en vertical u horizontal (no en diagonal), y cada barco no puede solaparse con el contrario. La lista de barcos es:
- PortaAviones (A), que usa 5 casillas.
- Buque (B), que usa 4 casillas.
- Submarino (S) y Cruzero (C), que usan 3 casillas cada una.
- Y un Destructor (D), que usa 2 casillas.

Un ejemplo de tablero puede ser:

- 0 D 0 0 0 0 0 0 0 0
- 0 D 0 0 A A A A A 0
- 0 0 0 0 0 0 0 0 0 0
- 0 0 0 0 0 B 0 0 0 0
- 0 0 0 0 0 B 0 0 0 0
- 0 0 0 0 0 B 0 0 0 0
- 0 0 0 0 0 0 0 0 0 0
- 0 C C C 0 0 0 0 0 0
- 0 0 0 0 0 0 0 S S S
- 0 0 0 0 0 0 0 0 0 0

El oceano se encuentra escondido para usted (privado en la clase Tablero.java).

Creditos
- Vídeo: https://youtu.be/LbALFZoRrw8
- Documento: http://www.datagenetics.com/blog/december32011/
