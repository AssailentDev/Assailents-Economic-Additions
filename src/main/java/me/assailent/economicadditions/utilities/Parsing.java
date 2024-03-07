package me.assailent.economicadditions.utilities;

import me.assailent.economicadditions.EconomicAdditions;

public class Parsing {
    public String parse(String input, String parse, String currency) {
        String[] split = input.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i != 0) {
                sb.append(" ");
            }
            if (split[i].equals("%parse%")) {
                sb.append(parse);
            } else if (split[i].equals("%parse%'s")) {
                sb.append(parse + "'s");
            } else if (split[i].equals("%currency%")) {
                sb.append(currency);
            } else {
                sb.append(split[i]);
            }
        }
        return sb.toString();
    }
}
