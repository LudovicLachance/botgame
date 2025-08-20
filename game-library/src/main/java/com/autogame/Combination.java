package com.autogame;

import java.util.ArrayList;
import java.util.List;

public class Combination {
  public static <T> List<List<T>> combine(List<T> inputSet, int n) {
    List<List<T>> results = new ArrayList<>();
    combinationsInternal(inputSet, n, results, new ArrayList<>(), 0);
    return results
        .stream()
        .filter(c -> c.size() == n)
        .toList();
  }

  private static <T> void combinationsInternal(
      List<T> inputSet, int k, List<List<T>> results, List<T> accumulator, int index) {
    int needToAccumulate = k - accumulator.size();
    int canAcculumate = inputSet.size() - index;

    if (accumulator.size() == k) {
      results.add(new ArrayList<>(accumulator));
    } else if (needToAccumulate <= canAcculumate) {
      combinationsInternal(inputSet, k, results, accumulator, index + 1);
      accumulator.add(inputSet.get(index));
      combinationsInternal(inputSet, k, results, accumulator, index + 1);
      accumulator.removeLast();
    }
  }
}
