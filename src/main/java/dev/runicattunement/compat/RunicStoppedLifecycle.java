package dev.runicattunement.compat;

import java.lang.reflect.Proxy;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopped;

/**
 * Registers against Fabric's loaded callback interface, avoiding a mismatch
 * between the intermediary MinecraftServer descriptor in the recovered source
 * and the named descriptor used by the runtime Fabric lifecycle API.
 */
final class RunicStoppedLifecycle {
    private RunicStoppedLifecycle() {}

    static void register() {
        ServerStopped listener = (ServerStopped) Proxy.newProxyInstance(
            ServerStopped.class.getClassLoader(),
            new Class<?>[] { ServerStopped.class },
            (proxy, method, arguments) -> {
                if (method.getName().equals("onServerStopped") && method.getParameterCount() == 1) {
                    RunicCompatibility.runicShutdownClear();
                    return null;
                }
                if (method.getDeclaringClass() == Object.class) {
                    return switch (method.getName()) {
                        case "equals" -> proxy == arguments[0];
                        case "hashCode" -> System.identityHashCode(proxy);
                        case "toString" -> "RunicAttunementServerStoppedListener";
                        default -> throw new UnsupportedOperationException(method.toString());
                    };
                }
                throw new UnsupportedOperationException(method.toString());
            });
        ServerLifecycleEvents.SERVER_STOPPED.register(listener);
    }
}
