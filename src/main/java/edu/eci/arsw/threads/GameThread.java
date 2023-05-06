package edu.eci.arsw.threads;

import edu.eci.arsw.models.BloqueTetris;
import edu.eci.arsw.models.Estado;
import edu.eci.arsw.models.Lobby;
import edu.eci.arsw.models.Tablero;
import edu.eci.arsw.models.buffos.CommonBuffo;
import edu.eci.arsw.models.buffos.factories.BuffoFactory;
import edu.eci.arsw.models.player.Player;
import edu.eci.arsw.notifiers.PlayerNotifier;
import edu.eci.arsw.shared.TetrisException;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class GameThread extends Thread{

    private final Lobby lobby;
    private final AtomicBoolean playersMoved;

    private final List<PlayerNotifier> players = new ArrayList<>();

    private final TimerTask genBuffo;
    private final Timer timer;

    private final List<String> colorsTableros;

    private final CommonBuffo b = new CommonBuffo();

    public int getCodigo(){
        return lobby.getCodigo();
    }

    public GameThread(Lobby lobby, AtomicBoolean playersMoved){
        this.lobby = lobby;
        this.playersMoved = playersMoved;
        this.colorsTableros = lobby.getColorsTableros();
        timer = new Timer();
        genBuffo = instanceBuffosTask();
    }


    @Override
    public void run(){
        instanceGame();
        while(!lobby.endGame()){
            for (PlayerNotifier player : players) {
                try {
                    synchronized (player.getMoved()){
                        player.getMoved().set(false);
                        player.moveBlock("DOWN");
                        player.getMoved().set(true);
                        player.getMoved().notify();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
            synchronized (playersMoved){
                playersMoved.set(true);
                playersMoved.notify();
            }

            try {
                Thread.sleep(lobby.getVelocity());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("end");
        genBuffo.cancel();
        timer.cancel();
        lobby.setEstado(Estado.FINISHED);
    }


    /**
     * Inicializa los tableros de los jugadores con una misma lista de bloques
     */
    private void instanceGame(){
        List<BloqueTetris> bloques = Collections.synchronizedList(new ArrayList<>());
        List<Tablero> tableros = new ArrayList<>();
        Player player;

        for (int i = 0; i < lobby.getPlayers().size(); i++) {
             player = lobby.getPlayers().get(i);
            tableros.add(new Tablero(true, lobby.getVelocity(), player.getColorTablero(),
                    lobby.getFilas(), lobby.getCols(), bloques, b, tableros));
            player.setTablero(tableros.get(i));
            players.add(new PlayerNotifier(player, new AtomicBoolean(false)));
        }
        timer.schedule(genBuffo, 10000, 10000);
        lobby.setEstado(Estado.RUNNING);
    }

    public void moveBlock(String username, String movement) {
        PlayerNotifier playerNot = players.stream().filter(p -> Objects.equals(p.getPlayer().getNick(), username))
                .collect(Collectors.toList()).get(0);
        try {
            GameModifyThread gmt = new GameModifyThread(playerNot, movement);
            gmt.start();
            gmt.join();
        } catch (InterruptedException ignored) {}
    }

    private TimerTask instanceBuffosTask(){
        return new TimerTask() {
            public void run() {
                int iteraciones = 0;
                this.instanceBuffo(iteraciones);
            }

            private void instanceBuffo(int iteraciones){
                int[] c = this.getCoord();
                if (this.isEmptyCoord(c) || iteraciones >= 10) b.set(BuffoFactory.getRandomBuffo(c, colorsTableros));
            }

            private int[] getCoord(){
                return new int[]{(int) (Math.random()*lobby.getFilas()), (int) (Math.random()*lobby.getCols())};
            }

            private boolean isEmptyCoord(int[] coord) {
                boolean isEmpty = true;
                int i = 0;
                while (i < players.size()){
                    PlayerNotifier p = players.get(i);
                    synchronized (p.getMoved()){
                        if (p.isEmptyCoord(coord)) {
                            isEmpty = false;
                            break;
                        }
                    }
                    i++;
                }
                return isEmpty;
            }
        };

    }


}

