package nz.co.doltech.databinder.databinding;

/**
 * Interface for converting a value between two types. Those two types are not specified
 * at compile time and will be discovered at runtime.
 *
 * @author Arnaud Tournier
 *         (c) LTE Consulting - 2015
 *         http://www.doltech.co.nz
 */
public interface BidirectionalConverter<A, B> extends Converter<A, B> {
    /**
     * Get the reversed version of the converter.
     */
    Converter<B, A> reverse();
}
