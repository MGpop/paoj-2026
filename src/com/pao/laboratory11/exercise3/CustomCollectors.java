package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public final class CustomCollectors {
    private CustomCollectors() {}

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Agg {
            Map<String, Long> countByCountry = new HashMap<>();
            Map<String, Long> countByChannel = new HashMap<>();
            Map<String, BigDecimal> totalByCountry = new HashMap<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<Transaction> transactions = new ArrayList<>();

            void add(Transaction tx) {
                countByCountry.merge(tx.getCountry(), 1L, Long::sum);
                countByChannel.merge(tx.getChannel(), 1L, Long::sum);

                totalByCountry.merge(
                        tx.getCountry(),
                        tx.getAmount(),
                        BigDecimal::add
                );

                totalAmount = totalAmount.add(tx.getAmount());
                transactions.add(tx);
            }

            Agg combine(Agg other) {
                other.countByCountry.forEach((k, v) ->
                        countByCountry.merge(k, v, Long::sum));

                other.countByChannel.forEach((k, v) ->
                        countByChannel.merge(k, v, Long::sum));

                other.totalByCountry.forEach((k, v) ->
                        totalByCountry.merge(k, v, BigDecimal::add));

                totalAmount = totalAmount.add(other.totalAmount);
                transactions.addAll(other.transactions);

                return this;
            }

            Snapshot finish() {
                List<Transaction> top = transactions.stream()
                        .sorted((a, b) -> {
                            int amountCompare = b.getAmount().compareTo(a.getAmount());
                            if (amountCompare != 0) {
                                return amountCompare;
                            }
                            return Integer.compare(a.getId(), b.getId());
                        })
                        .limit(topN)
                        .toList();

                return new Snapshot(
                        countByCountry,
                        countByChannel,
                        totalByCountry,
                        totalAmount,
                        top
                );
            }
        }

        return Collector.of(
                Agg::new,
                Agg::add,
                Agg::combine,
                Agg::finish
        );
    }
}