# Poobtriz


Tetris es un juego de puzzle muy popular que fue creado en 1984 por el programador ruso, Alexey Pajitnov. El objetivo del juego es colocar piezas de diferentes formas llamadas "tetrominos" en una matriz de juego y completar líneas horizontales sin espacios vacíos. A medida que se completan las líneas, se eliminan del juego y se otorgan puntos al jugador.

Por otra parte, se desarrolló un programa que permite jugar una versión alternativa al tetris tradicional, con la posibilidad de jugar en línea, donde se puede jugar a través de Internet en un navegador web. En POOBTriz se usarán bloques de diferentes formas, en el cual los tetrónimos pueden ser de diferentes tipos y son reconocidos por su reborde, es decir: 

- Si el tetronimo tiene el borde de color **negro**, no se generá ninguna acción especial. 
- Si se completa una línea con un bloque donde los tetronimos tegan borde **plateado**, la línea no desaparecerá. 
- Si el borde el **dorado**, se transforman en en el mejor bloque para el lugar donde se ubican.
- Cuando el bloque se fije y el tetronimo tenga borde **rojo** se autodestruirá y destruirán los bloques que este
toque.

De igual manera, en este programa el jugador tendrá la posibilidad de  seleccionar una velocidad inicial para que el juego transcurra con esta misma velocida y de igual manera tendrán la posibilidad de visualizar los "buffos". Los buffos aparecerán cada cierto tiempo en un espacio especial de la zona de juego de manera aleatoria para que los jugadores en ningún momento conozcan cada cuánto aparecerá un buffo ni de qué tipo será.



## Integrantes

- Carol Cely Rodriguez
- Juan Pablo Fonseca
- Santiago Cardenas Amaya

## Descripcion

### Antecedentes 

El código hace parte de un desarrollo realizado en la materia POOB. De esa lógica solo se hará uso del back.

Inicialmente viene con problemas de sincronización para el modo multijugador, puesto que el tema de concurrencia y paralelismo no fue implementado de forma adecuada


![image.png](./image-8b59abe4-e7b1-45fa-8d82-38b49fc8f953.png)

### Problema a resolver 

Los problemas identificados que se pueden resolver a tener un tetris en lineal son los siguientes: 

1. Accesibilidad: un Tetris en línea debería ser jugado por cualquier persona con acceso a Internet y un navegador web, lo que significa que el juego puede ser disfrutado por una audiencia mucho más amplia.

2. Interacción social: una versión en línea del Tetris debería permitir a los jugadores interactuar entre sí. Esto puede llegar a generar una experiencia social y una sensación de comunidad para los jugadores.

3. Variedad: una versión en línea del Tetris debería ofrecer una amplia variedad de opciones y características, como modos de juego adicionales, opciones de personalización, diferentes escenarios en cuanto a piezas, variedad de funcionalidades y gráficos mejorados. Esto puede mantener el juego fresco e interesante para los jugadores. 



### Buffos
Un buffo es un atributo del juego que le aparecerá a varios jugadores. Estos aparecen cada 10 segundos y se reconocen porque no tienen un color plano.

Para simplificar, se considera invocador aquel que activa un buffo

| Buffo     | Descripción                                                                                                      |
|-----------|------------------------------------------------------------------------------------------------------------------|
| LessScore | La puntuación que consigan los jugadores se verá disminuida en un 50% por 10 segundos a excepción del invocador. |
| StopDiece | Se detienen los bloques de los jugadores a excepción del invocador, hasta que se presione la tecla de bajar.     |
| Slow      | Los jugadores caerán el doble de lento a excepción del invocador por 5 segundos                                  |
| 2x        | El jugador que lo coja bajará el doble de rápido.                                                                |



### Rebordes
Se refiere al contorno del tetrómino. Según el color de este, la ficha se comportará de distintas maneras al momento de hacer contacto con cuadros del tablero que no permitan bajar más:

| Reborde    | Color | Descripción                                                  |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Classic | Negro | Borde normal. |
| Useless | Plateado | Si se completa una línea con este bloque, la línea no desaparecerá |
| Winner     | Dorado | Se transforman en el mejor bloque para el lugar donde se ubican. |
| Bomb          | Rojo      | Cuando el bloque se fije se autodestruiré y destruirán los bloques que este toque |


