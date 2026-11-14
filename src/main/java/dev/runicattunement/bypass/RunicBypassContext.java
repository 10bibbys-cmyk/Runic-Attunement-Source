package dev.runicattunement.bypass;

public final class RunicBypassContext {
   private static final ThreadLocal<Integer> FISHING_DEPTH = ThreadLocal.withInitial(() -> 0);

   private RunicBypassContext() {
   }

   public static void enterFishing() {
      FISHING_DEPTH.set(FISHING_DEPTH.get() + 1);
   }

   public static void exitFishing() {
      int next = FISHING_DEPTH.get() - 1;
      if (next <= 0) {
         FISHING_DEPTH.remove();
      } else {
         FISHING_DEPTH.set(next);
      }
   }

   public static boolean isFishing() {
      return FISHING_DEPTH.get() > 0;
   }
}
