package com.example.assignment1_q4;
import java.io.Serializable;

public class Matrix implements Serializable {
    private String[][] matrix;

    public Matrix(){
        matrix = new String[3][3];
        for (int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                set(i,j,"-");
            }
        }
    }

    public Matrix(int size){
        matrix = new String[size][size];
        for (int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[i].length; j++){
                set(i,j,"-");
            }
        }
    }

    public void set (int rowIndex, int colIndex, String data){
        try {
            matrix[rowIndex][colIndex] = data;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Array out of bound...");
        }
    }

    public String get(int rowIndex, int colIndex){
        return matrix[rowIndex][colIndex];
    }
}