package io.dropwizard.java8.auth;

import io.dropwizard.auth.Auth;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.internal.inject.AbstractValueFactoryProvider;
import org.glassfish.jersey.server.internal.inject.MultivaluedParameterExtractorProvider;
import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class AuthFactoryProvider<C, P> extends AbstractValueFactoryProvider {
    private final AuthFactory<C, P> factory;

    @Inject
    public AuthFactoryProvider(final MultivaluedParameterExtractorProvider extractorProvider,
                               final AuthFactory<C, P> factory,
                               final ServiceLocator injector) {
        super(extractorProvider, injector, Parameter.Source.UNKNOWN);
        this.factory = factory;
    }

    @Override
    protected Factory<?> createValueFactory(final Parameter parameter) {
        final Class<?> classType = parameter.getRawType();
        final Auth auth = parameter.getAnnotation(Auth.class);

        if (auth != null && classType.isAssignableFrom(factory.getGeneratedClass())) {
            return factory.clone(auth.required());
        } else {
            return null;
        }
    }

    public static class AuthInjectionResolver extends ParamInjectionResolver<Auth> {
        public AuthInjectionResolver() {
            super(AuthFactoryProvider.class);
        }
    }

    public static class Binder<T, U> extends AbstractBinder {
        private AuthFactory<T, U> factory;

        public Binder(AuthFactory<T, U> factory) {
            this.factory = factory;
        }

        @Override
        protected void configure() {
            bind(this.factory).to(AuthFactory.class);
            bind(AuthFactoryProvider.class).to(ValueFactoryProvider.class).in(Singleton.class);
            bind(AuthInjectionResolver.class).to(
                    new TypeLiteral<InjectionResolver<Auth>>() {
                    }
            ).in(Singleton.class);
        }
    }
}

