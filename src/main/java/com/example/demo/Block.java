package com.example.demo;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Block {
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;
    private Color color;
    private String name;
    int state=1;
    public Block(Rectangle a,Rectangle b,Rectangle c, Rectangle d,String name){
        this.a=a;
        this.b=b;
        this.c=c;
        this.d=d;
        this.name=name;

        switch(name){
            case "i":
                color=Color.FUCHSIA;
                break;
            case "j":
                color=Color.TURQUOISE;
                break;
            case "l":
                color=Color.MEDIUMPURPLE;
                break;
            case "o":
                color=Color.DARKBLUE;
                break;
            case "s":
                color=Color.DARKSLATEGRAY;
                break;
            case "t":
                color=Color.LIGHTGRAY;
                break;
            case "z":
                color=Color.INDIGO;
                break;
        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }
    public String getName(){
        return name;
    }
    public void rotate(){
        if(state<4){
            state++;
        }
        else{
            state=1;
        }
    }
}