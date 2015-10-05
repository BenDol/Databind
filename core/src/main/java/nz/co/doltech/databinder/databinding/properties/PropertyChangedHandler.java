package nz.co.doltech.databinder.databinding.properties;

/**
 * Interface through which one receives {@link PropertyChangedEvent}
 */
public interface PropertyChangedHandler {
    void onPropertyChanged(PropertyChangedEvent event);
}