package InputHandling;

/**Defines an Interface for an object that can return a value with the return type
 * deferred to the concrete implementation.
 * @param <T> Generic return type defined by concrete implementation.
 *           Primitive return types not allowed with this interface.
 */
@SuppressWarnings("ALL")
interface Valued<T> {
    T getValue();
}
