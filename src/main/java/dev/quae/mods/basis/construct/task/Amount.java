package dev.quae.mods.basis.construct.task;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.function.IntPredicate;

public interface Amount extends IntPredicate {

  Amount ZERO = new Const(0);

  Codec<Amount> CODEC = Codec.either(Const.CODEC, Range.CODEC).comapFlatMap(
      value -> DataResult.success(value.left().isPresent() ? value.left().get() : value.right().isPresent() ? value.right().get() : ZERO),
      value -> value instanceof Const ? Either.left((Const) value) : value instanceof Range ? Either.right((Range) value) : Either.left((Const) ZERO)
  );

  int lowerBound();

  int upperBound();

  Codec<? extends Amount> codec();

  class Range implements Amount {

    static Codec<Range> CODEC = RecordCodecBuilder.create(range -> range.group(
        Codec.INT.fieldOf("min").forGetter(it -> it.min),
        Codec.INT.fieldOf("max").forGetter(it -> it.max)
    ).apply(range, Range::new));

    private final int min;
    private final int max;

    public Range(int min, int max) {
      this.min = min;
      this.max = max;
    }

    @Override
    public boolean test(int value) {
      return this.min <= value && value <= this.max;
    }

    @Override
    public int lowerBound() {
      return min;
    }

    @Override
    public int upperBound() {
      return max;
    }

    @Override
    public Codec<Range> codec() {
      return CODEC;
    }
  }

  class Const implements Amount {

    static Codec<Const> CODEC = Codec.INT.comapFlatMap(it -> DataResult.success(new Const(it)), Const::lowerBound);

    private final int value;

    public Const(int value) {
      this.value = value;
    }

    @Override
    public boolean test(int value) {
      return this.value == value;
    }

    @Override
    public int lowerBound() {
      return value;
    }

    @Override
    public int upperBound() {
      return value;
    }

    @Override
    public Codec<Const> codec() {
      return CODEC;
    }
  }
}