package cz.novosadkry.Pickaxe3X3.EventHandlers;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak3x3Event extends Event {
    private BlockBreakEvent event;
    private int rows;
    private int columns;

    private static final HandlerList HANDLERS = new HandlerList();

    public BlockBreak3x3Event(int rows, int columns, BlockBreakEvent event) {
        this.event = event;
        this.rows = rows;
        this.columns = columns;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public BlockBreakEvent getBlockBreakEvent() {
        return event;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
