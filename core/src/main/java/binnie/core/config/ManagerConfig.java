package binnie.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import binnie.core.util.Log;

import binnie.core.AbstractMod;
import binnie.core.ManagerBase;

public class ManagerConfig extends ManagerBase {
	public ManagerConfig() {}

	public void registerConfiguration(final Class<?> cls, final AbstractMod mod) {
		if (cls.isAnnotationPresent(ConfigFile.class)) {
			this.loadConfiguration(cls, mod);
		}
	}

	public void loadConfiguration(final Class<?> cls, final AbstractMod mod) {
		final String filename = cls.getAnnotation(ConfigFile.class).filename();
		final BinnieConfiguration config = new BinnieConfiguration(filename, mod);
		config.load();
		for (final Field field : cls.getFields()) {
			if (field.isAnnotationPresent(ConfigProperty.class)) {
				final ConfigProperty propertyAnnot = field.getAnnotation(ConfigProperty.class);
				for (final Annotation annotation : field.getAnnotations()) {
					if (annotation.annotationType().isAnnotationPresent(ConfigProperty.Type.class)) {
						final Class<?> propertyClass = annotation.annotationType().getAnnotation(ConfigProperty.Type.class).propertyClass();
						try {
							Constructor<?> constructor = propertyClass.getConstructor(Field.class, BinnieConfiguration.class, ConfigProperty.class, annotation.annotationType());
							Annotation cast = annotation.annotationType().cast(annotation);
							final PropertyBase<?, ?> property = (PropertyBase<?, ?>) constructor.newInstance(field, config, propertyAnnot, cast);
						} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
							Log.error("Failed to load configuration for property {}", propertyClass, e);
						}
					}
				}
			}
		}
		config.save();
	}
}
