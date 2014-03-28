package com.dissonance.dialogcreator.style;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list that holds styles and provides options to add and manage them.
 */
public final class StyleList {

    private final List<StyleRange> styleRanges = new ArrayList<>();

    public StyleRange get(int index) {
        return styleRanges.get(index);
    }

    /**
     * Adds the specified {@link StyleRange} to the list, {@link #merge(StyleRange) merges} it
     * with the existing ones and {@link #optimize() optimizes} the list.
     *
     * @param styleRange The {@link StyleRange} to add. If the list already contains
     *                   the specified StyleRange, it will be rejected.
     */
    public synchronized void add(StyleRange styleRange) {

        styleRanges.add(styleRange);
        merge(styleRange);
        optimize();

        Collections.sort(styleRanges);
    }

    /**
     * Removes the specified {@link StyleRange} from the list.
     *
     * @param styleRange The {@link StyleRange} to remove.
     */
    public synchronized void remove(StyleRange styleRange) {
        styleRanges.remove(styleRange);
    }

    /**
     * Removes all the {@link StyleRange StyleRanges} within the specified selection.
     * Trims the ones that only intersect it partially.
     *
     * @param start The start of the range to remove.
     * @param end   The end of the range to remove.
     */
    public synchronized void remove(int start, int end) {
        List<StyleRange> intersects = getIntersects(start, end);

        if (intersects.size() == 0) {
            return;
        }

        StyleRange first = intersects.get(0);
        StyleRange last = intersects.get(intersects.size() - 1);

        if (first.getStart() < start) {
            styleRanges.add(first.derive(first.getStart(), start - 1));
        }

        if (last.getEnd() > end) {
            int length = end - start + 1;
            styleRanges.add(last.derive(end - length + 1, last.getEnd() - length));
        }

        styleRanges.removeAll(intersects);

        optimize();
    }

    /**
     * Inserts the specified {@link StyleRange} into the list, splitting any StyleRanges
     * that it intersects.
     *
     * @param target The {@link StyleRange} to insert.
     */
    public synchronized void insert(StyleRange target) {
        if (styleRanges.size() == 0) {
            styleRanges.add(target);
            return;
        }

        int length = target.getEnd() - target.getStart() + 1;
        boolean added = false;

        List<StyleRange> ranges = new ArrayList<>();
        List<StyleRange> remove = new ArrayList<>();

        for (StyleRange styleRange : styleRanges) {
            if (styleRange.getEnd() + length < target.getStart()) {
                continue;
            }

            if (styleRange.getStart() + length > target.getEnd() && styleRange.getEnd() + length > target.getEnd()) {
                ranges.add(styleRange.derive(styleRange.getStart() + length, styleRange.getEnd() + length));
                remove.add(styleRange);
                continue;
            }

            remove.add(styleRange);
            ranges.add(styleRange.derive(styleRange.getStart(), styleRange.getEnd() + length));
            added = true;
        }

        if (!added) {
            styleRanges.add(target);
        }

        styleRanges.removeAll(remove);
        styleRanges.addAll(ranges);

        optimize();
    }

    public int size() {
        return styleRanges.size();
    }

    /**
     * Returns all the {@link StyleRange StyleRanges} that intersect the specified StyleRange.
     *
     * @param target The {@link StyleRange} to check for.
     */
    private List<StyleRange> getIntersects(StyleRange target) {
        List<StyleRange> intersects = new ArrayList<>();

        for (StyleRange range : styleRanges) {
            if (!target.equals(range) && range.getStart() < target.getEnd() && target.getStart() < range.getEnd()) {
                intersects.add(range);
            }
        }

        return intersects;
    }

    /**
     * Returns all the {@link StyleRange StyleRanges} that intersect the specified offset.
     *
     * @param start The start of the offset.
     * @param end   The end of the offset.
     */
    private List<StyleRange> getIntersects(int start, int end) {
        List<StyleRange> intersects = new ArrayList<>();

        for (StyleRange range : styleRanges) {
            if (range.getStart() <= end && start <= range.getEnd()) {
                intersects.add(range);
            }
        }

        return intersects;
    }

    /**
     * Merges the specified {@link StyleRange} into the list by using
     * {@link StyleRange#merge(StyleRange)} on all of the StyleRanges that intersect it.
     *
     * @param styleRange The {@link StyleRange} to merge.
     */
    private void merge(StyleRange styleRange) {
        List<StyleRange> intersects = getIntersects(styleRange);
        for (StyleRange r : intersects) {
            List<StyleRange> ranges = r.merge(styleRange);
            if (ranges.size() > 0) {
                styleRanges.remove(styleRange);
                styleRanges.remove(r);
                styleRanges.addAll(ranges);

                for (StyleRange range : ranges) {
                    merge(range);
                }
                break;
            }
        }
    }

    /**
     * Optimizes the list by joining all the adjacent {@link StyleRange StyleRanges} with the same
     * style and color into one StyleRange.
     */
    private void optimize() {
        boolean performed = false;
        Collections.sort(styleRanges);

        for (int i = 0; i < styleRanges.size() - 1; i++) {
            StyleRange current = styleRanges.get(i);
            StyleRange following = styleRanges.get(i + 1);

            if (current.getEnd() + 1 == following.getStart() || current.getEnd() == following.getStart()) {
                if (current.getColor() == null && following.getColor() == null) {
                    styleRanges.add(current.derive(current.getStart(), following.getEnd()));
                    styleRanges.remove(current);
                    styleRanges.remove(following);

                    performed = true;
                    break;
                }

                if (current.getColor() == null || following.getColor() == null) {
                    continue;
                }

                if (current.getStyle() == following.getStyle() && current.getColor().equals(following.getColor())) {
                    styleRanges.add(current.derive(current.getStart(), following.getEnd()));
                    styleRanges.remove(current);
                    styleRanges.remove(following);

                    performed = true;
                    break;
                }
            }
        }

        if (performed) {
            optimize();
        }
    }
}
