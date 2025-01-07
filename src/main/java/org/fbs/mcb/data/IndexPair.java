package org.fbs.mcb.data;

/**
 * A record representing a pair of indices.
 * This class is used to store and manipulate pairs of integer values, typically representing input and output indices.
 * @see ClassReorder
 */
public record IndexPair(int inputIndex, int outputIndex) {}