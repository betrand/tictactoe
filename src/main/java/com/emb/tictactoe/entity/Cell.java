package com.emb.tictactoe.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.enterprise.inject.Model;

/**
 *
 * @author bu_000
 */
@Model
public class Cell implements Serializable {

    private Integer id;
    private int row;
    private int col;
    private String mark;

    public Cell(int id, int row, int col, String mark) {
        this.id = id;
        this.row = row;
        this.col = col;
        this.mark = mark;
    }

    public Cell(int i, int i0) {
        this.row = row;
        this.col = col;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        return Objects.equals(this.id, other.id);
    }

    public boolean equals(Object obj, String s) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cell other = (Cell) obj;
        if (this.row != other.row) {
            return false;
        }
        return this.col == other.col;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Cell{");
        sb.append(" id=").append(this.id);
        sb.append("x=").append(this.row);
        sb.append(", y=").append(this.col);
        sb.append(", mark=").append(this.mark);
        sb.append('}');
        return sb.toString();
    }

}
