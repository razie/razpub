/**
 * Razvan's public code. Copyright 2008 based on Apache license (share alike) see LICENSE.txt for
 * details.
 */
package com.razie.pub.draw;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.razie.pub.draw.DrawStream.Element.State;
import com.razie.pub.draw.Renderer.ContainerRenderer;
import com.razie.pub.draw.Renderer.Technology;

/**
 * a drawable table of rows of lists. objects must implement Drawable or else we'll use toString()
 * 
 * an interesting feature: if hooked up to an output stream, it will write as objects are added to
 * it. in that case you should call close() to tell it it's done...
 * 
 * it works in two modes: setRows() or add() elements
 * 
 * @author razvanc99
 * 
 */
@SuppressWarnings("unchecked")
public class DrawTable extends StreamableContainer.Impl implements Drawable, StreamableContainer {

    private List        rows       = new ArrayList();
    public String       rowColor   = null;
    public String       htmlWidth  = "";

    public OutputStream outs       = null;

    public int          prefRows   = -1;
    public int          prefCols   = -1;
    public boolean      packed     = false;

    private int         lastAddRow = -1;
    private int         lastAddCol = -1;

    public HorizAlign   horizAlign = HorizAlign.CENTER;

    public static enum HorizAlign {
        LEFT, CENTER, RIGHT
    };

    public DrawTable() {
    }

    /** if you prefer 4 columns, use (0,4) for instance. NOTE prefRows doesn't work */
    public DrawTable(int prefRows, int prefCols) {
        this.prefCols = prefCols;
        this.prefRows = prefRows;
    }

    /** shortcut to render self - don't like controllers that much */
    public Object render(Technology t, DrawStream stream) {
        return Renderer.Helper.draw(this, t, stream);
    }

    public Renderer getRenderer(Technology technology) {
        return new MyRenderer();
    }

    /**
     * @param list the list to set
     */
    public void setRows(List list) {
        this.rows = list;
    }

    /**
     * @return the list
     */
    public List getRows() {
        return rows;
    }

    /** add an object - table is arranged as objects are added based on prefRows/prefCols */
    public void writeAll(List l) {
        for (Object o : l) {
            write(o);
        }
    }

    /**
     * add an object - table is arranged as objects are added based on prefRows/prefCols
     * 
     * TODO WARNING: the table will actually render when a row is full, not every time you call this -
     * SCREWY side effects
     */
    public void write(Object o) {
        DrawList row = null;

        if (lastAddRow == -1 || (prefCols > 0 && lastAddCol >= prefCols - 1)) {
            // first time
            // close the previous row...
            if (lastAddRow >= 0) {
                row = (DrawList) this.rows.get(lastAddRow);
                row.close();
                if (this.ownerStream != null && this.state.equals(State.OPEN)) {
                    this.ownerStream.renderElement(this, row);
                }
            }

            // add new row
            lastAddRow++;
            row = new DrawList();
            // if (this.ownerStream != null && this.state.equals(State.OPEN)) {
            // row.open(this.ownerStream);
            // }
            this.rows.add(row);
            lastAddCol = 0;
        } else {
            row = (DrawList) this.rows.get(lastAddRow);
            lastAddCol++;
        }

        row.write(o);
    }

    public void close() {
        if (this.ownerStream != null && this.state.equals(State.OPEN)) {
            if (lastAddRow < 0 || (prefCols > 0 && lastAddCol > prefCols - 1)) {
            } else if (!this.wroteElements) {
                DrawList row = (DrawList) this.rows.get(lastAddRow);
                row.close();
                this.ownerStream.renderElement(this, row);
            }
        }

        this.wroteElements = true;
        super.close();
    }

    public class MyRenderer implements ContainerRenderer {

        public boolean canRender(Object o, Technology technology) {
            return o instanceof DrawTable;
        }

        public Object render(Object o, Technology technology, DrawStream stream) {
            DrawTable table = (DrawTable) o;
            String s = (String) renderHeader(o, technology, stream);
            for (Object row : table.getRows()) {
                s += (String) renderElement(o, row, technology, stream);
            }
            s += (String) renderFooter(o, technology, stream);
            return s;
        }

        public Object renderElement(Object container, Object row, Technology technology, DrawStream stream) {
            DrawTable table = (DrawTable) container;
            if (Technology.HTML.equals(technology)) {
                String s = makeTR(table);
                if (row instanceof DrawList) {
                    DrawList list = (DrawList) row;
                    for (Object e : list.getList()) {
                        s += "<td";
                        String width = table.packed ? "" : "width=200";
                        s += table.prefCols > 0 ? " align=" + table.horizAlign.toString().toLowerCase() + " "
                                + width + ">" : ">";
                        s += Renderer.Helper.draw(e, Technology.HTML, stream);
                        s += "</td>";
                    }
                } else {
                    s += "<td>";
                    s += Renderer.Helper.draw(row, Technology.HTML, stream);
                    s += "</td>";
                }

                s += "</tr>\n";
                return s;
            } else {
                String s = "";
                if (row instanceof DrawList) {
                    DrawList list = (DrawList) row;
                    for (Object e : list.getList()) {
                        s += Renderer.Helper.draw(e, technology, stream);
                    }
                } else {
                    s += Renderer.Helper.draw(row, technology, stream);
                }

                return s;
            }
        }

        public Object renderFooter(Object o, Technology technology, DrawStream stream) {
            if (Technology.HTML.equals(technology)) {
                return "</table>\n";
            }

            return "?";
        }

        public Object renderHeader(Object o, Technology technology, DrawStream stream) {
            DrawTable table = (DrawTable) o;
            if (Technology.HTML.equals(technology)) {
                return "<table valign=center" + table.htmlWidth + ">\n";
            }

            return "?";
        }
    }

    public void writeRow(Object o) {
        this.rows.add(o);
        if (this.ownerStream != null && this.state.equals(State.OPEN)) {
            this.ownerStream.renderElement(this, o);
        }
    }

    static String makeTR(DrawTable list) {
        return (list.rowColor == null ? "<tr>" : "<tr bgcolor=\"" + list.rowColor + "\">");
    }

}
