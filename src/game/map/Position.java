// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.map;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a position on the game map using row and column coordinates.
 * Includes internal locking for synchronized access to this location.
 */
public class Position {
    private int row, col;
    private final ReentrantLock lock = new ReentrantLock(true); // fair locking

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Position copy() {
        return new Position(this.row, this.col);
    }


    public boolean tryLock(long timeoutMillis) {
        try {
            return lock.tryLock(timeoutMillis, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    public boolean isLocked() {
        return lock.isLocked();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int distanceTo(Position other) {
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }
}
