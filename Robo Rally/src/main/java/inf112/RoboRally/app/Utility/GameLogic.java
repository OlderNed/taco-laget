package inf112.RoboRally.app.Utility;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import inf112.RoboRally.app.Grid.Board;
import inf112.RoboRally.app.Player.Player;


import java.util.Collections;

public class GameLogic implements ApplicationListener {

    private final Player player;


    private final TiledMapTileLayer holes;
    private final TiledMapTileLayer walls;
    private final TiledMapTileLayer flags;
    private final TiledMapTileLayer playerLayer;
    private final TiledMapTileLayer conveyors;

    public GameLogic(Player player, Board board) {

        this.player = player;
        this.holes = board.holeLayer;
        this.walls = board.walls;
        this.flags = board.flagLayer;
        this.conveyors = board.conveyorLayer;
        this.playerLayer = board.playerLayer;
    }


    public void update() {

        System.out.println("logic tick");

        if (holes.getCell((int) player.getPosition().x, (int) player.getPosition().y) != null) {
            player.setDamage(1);
        }
        if(flags.getCell((int) player.getPosition().x, (int)player.getPosition().y) != null) {

            int id = flagIdChecker(player.getPosition());
            if(player.flagsVisited.contains(id)){
                System.out.println("Already visited");
            }
            else if(player.flagsVisited.isEmpty() && id == 1){
                player.flagsVisited.add(id);
            }else if(!player.flagsVisited.isEmpty()){
                if(id > Collections.max(player.flagsVisited)&& player.flagsVisited.contains(id-1)){
                    player.flagsVisited.add(id);
                }
            }
        }
        forceMove();

        player.getScore();
        System.out.println(player.getPosition());

    }

    public void setPlayer(){
        playerLayer.setCell(player.getx(), player.gety(),player.getState());
    }

    public void clearPlayer(){
        playerLayer.setCell(player.getx(),player.gety(),null);
    }

    public void forceMove() {
        String dir = conveyorChecker(player.getPosition());
        while (!dir.equals("")) {
            if (dir.contains("DOWN")) {
                player.getPosition().add(0, -1);
            }
            else if (dir.contains("UP")) {
                player.getPosition().add(0, 1);
            }
            else if (dir.contains("LEFT")) {
                player.getPosition().add(-1, 0);
            }
            else if (dir.contains("RIGHT")) {
                player.getPosition().add(1, 0);
            }
            dir = conveyorChecker(player.getPosition());

        }

    }



    @Override
    public void create() {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    //Check if outBounds
    public boolean outOfBounds(Vector2 pos) {
        if (pos.x < 0 || pos.y < 0) {
            return true;
        }
        return pos.x > 14 || pos.y > 14;
    }

    //Checker used to find wall-tile-properties

    public String dirChecker(Vector2 pos) {
        String gg = "";
        if (walls.getCell((int) pos.x, (int) pos.y) != null) {
            TiledMapTileLayer.Cell cell = walls.getCell((int) pos.x, (int) pos.y);
            gg = (String) cell.getTile().getProperties().get("DIRECTION");
            return gg;
        } else {
            return gg;
        }
    }


    public String conveyorChecker(Vector2 pos){
        String gg = "";
        if(conveyors.getCell((int) pos.x, (int) pos.y) != null){
            TiledMapTileLayer.Cell cell = conveyors.getCell((int) pos.x, (int) pos.y);
            gg = (String) cell.getTile().getProperties().get("DIRECTIONS");
            return gg;
        }else {
            return gg;
        }
    }

    public int flagIdChecker(Vector2 pos){

        if(flags.getCell((int) pos.x, (int) pos.y) != null){
            TiledMapTileLayer.Cell cell = flags.getCell((int) pos.x, (int)pos.y);
            return (int) cell.getTile().getProperties().get("ID");
        }else{
            return 0;
        }
    }
}