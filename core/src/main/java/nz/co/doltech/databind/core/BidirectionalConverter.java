package nz.co.doltech.databind.core;

/**
 * Interface for converting a value between two types. Those two types are not specified
 * at compile time and will be discovered at runtime.
 *
 * @author Ben Dol
 */
public interface BidirectionalConverter<A, B> extends Converter<A, B> {
    /**
     * Get the reversed version of the converter.
     */
    Converter<B, A> reverse();
}
