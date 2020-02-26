package cz.novosadkry.Pickaxe3X3.EventHandlers;

import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak3x3Event extends Event {
    private BlockBreakEvent event;
    private Block[] blocks;
    private int rows;
    private int columns;

    private static final HandlerList HANDLERS = new HandlerList();

    public BlockBreak3x3Event(BlockBreakEvent event, int rows, int columns, Block[] blocks) {
        this.event = event;
        this.blocks = blocks;
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

    public Block[] getBlocks() {
        return blocks;
    }
}
