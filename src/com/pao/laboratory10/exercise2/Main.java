package com.pao.laboratory10.exercise2;

import com.pao.laboratory10.exercise1.Tranzactie;
import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Tranzactie> tranzactii = new ArrayList<>();

        int n = Integer.parseInt(scanner.next());

        for (int i = 0; i < n; i++) {
            int id = Integer.parseInt(scanner.next());
            double suma = Double.parseDouble(scanner.next());
            String data = scanner.next();
            TipTranzactie tip = TipTranzactie.valueOf(scanner.next());

            tranzactii.add(new Tranzactie(id, suma, data, tip));
        }

        while (scanner.hasNext()) {
            String comanda = scanner.next();

            switch (comanda) {
                case "UNIQUE_IDS": {
                    LinkedHashSet<Integer> ids = new LinkedHashSet<>();

                    for (Tranzactie t : tranzactii) {
                        ids.add(t.getId());
                    }

                    System.out.println("IDs unice (" + ids.size() + "): " + ids);
                    break;
                }

                case "MONTHLY_REPORT": {
                    TreeMap<String, double[]> raport = new TreeMap<>();

                    for (Tranzactie t : tranzactii) {
                        String luna = t.getData().substring(0, 7);

                        if (!raport.containsKey(luna)) {
                            raport.put(luna, new double[2]);
                        }

                        double[] sume = raport.get(luna);

                        if (t.getTip() == TipTranzactie.CREDIT) {
                            sume[0] += t.getSuma();
                        } else {
                            sume[1] += t.getSuma();
                        }
                    }

                    for (Map.Entry<String, double[]> entry : raport.entrySet()) {
                        double[] sume = entry.getValue();

                        System.out.printf(Locale.US,
                                "%s: CREDIT %.2f RON, DEBIT %.2f RON%n",
                                entry.getKey(), sume[0], sume[1]);
                    }

                    break;
                }

                case "TOP": {
                    int topN = Integer.parseInt(scanner.next());

                    ArrayList<Tranzactie> copie = new ArrayList<>(tranzactii);
                    Collections.sort(copie, Comparator.comparingDouble(Tranzactie::getSuma).reversed());

                    System.out.println("Top " + topN + ":");

                    for (int i = 0; i < topN && i < copie.size(); i++) {
                        System.out.println(copie.get(i));
                    }

                    break;
                }

                case "SORT_ASC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    afiseazaLista(tranzactii);
                    break;
                }

                case "SORT_DESC": {
                    Collections.sort(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma).reversed());
                    afiseazaLista(tranzactii);
                    break;
                }

                case "REVERSE": {
                    Collections.reverse(tranzactii);
                    afiseazaLista(tranzactii);
                    break;
                }

                case "MIN_MAX": {
                    Tranzactie min = Collections.min(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));
                    Tranzactie max = Collections.max(tranzactii, Comparator.comparingDouble(Tranzactie::getSuma));

                    System.out.println("MIN: " + min);
                    System.out.println("MAX: " + max);
                    break;
                }

                case "CME_DEMO": {
                    try {
                        for (Tranzactie t : tranzactii) {
                            tranzactii.remove(t);
                        }
                    } catch (ConcurrentModificationException e) {
                        System.out.println("ConcurrentModificationException prins: modificare in iteratie detectata.");
                    }

                    break;
                }
            }
        }
    }

    private static void afiseazaLista(ArrayList<Tranzactie> tranzactii) {
        for (Tranzactie t : tranzactii) {
            System.out.println(t);
        }
    }
}