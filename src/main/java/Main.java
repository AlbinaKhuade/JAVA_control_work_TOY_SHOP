import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
//        Метод добавление новых игрушек и возможность изменения веса (частоты выпадения игрушки)
//        Возможность организовать розыгрыш игрушек.
//        Например, следующим образом:
//        С помощью метода выбора призовой игрушки – мы получаем эту призовую игрушку и записываем в список\массив.
//                Это список призовых игрушек, которые ожидают выдачи.
//        Еще у нас должен быть метод – получения призовой игрушки.
//                После его вызова – мы удаляем из списка\массива первую игрушку и сдвигаем массив. А эту игрушку записываем в текстовый файл.
//                Не забываем уменьшить количество игрушек
        ArrayList<Toy> listToys = new ArrayList<>();
        ArrayList<Toy> selectedListToys = new ArrayList<>();

        Toy t1 = new Toy(1, "Машинка", 10, 0.30);
        Toy t2 = new Toy(2, "Мишка", 10, 0.20);
        Toy t3 = new Toy(3, "Зайчик", 10, 0.10);
        Toy t4 = new Toy(4, "Кукла", 10, 0.40);
        Toy t5 = new Toy(5, "Трактор", 10, 0.60);

        listToys.add(t1);
        listToys.add(t2);
        listToys.add(t3);
        listToys.add(t4);
        listToys.add(t5);

        menu(listToys);
    }

    private static void menu(ArrayList<Toy> listToys) {
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("\nВведите пункт меню: ");
            System.out.println("1 - Показать все игрушки");
            System.out.println("2 - Добавить игрушку");
            System.out.println("3 - Изменить частоту выпадения игрушки");
            System.out.println("4 - Провести розыгрыш игрушек");
            System.out.println("0 - Выход");

            try {
                int choice = Integer.parseInt(in.nextLine());
                if (choice == 1) {
                    showAllToys(listToys);
                }
                if (choice == 2) {
                    addNewToy(listToys);
                }
                if (choice == 3) {
                    changeToyFrequency(listToys);
                }
                if (choice == 4) {
                    ArrayList<Toy> selectedListToys = addToyToSelectedList(listToys);
                    raffleToys(listToys, selectedListToys);
                }
                if (choice == 0) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели не число!");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void raffleToys(ArrayList<Toy> listToys, ArrayList<Toy> selectedListToys) throws IOException {
        Scanner in = new Scanner(System.in);
        String s = "-------------------------- Новый розыгрыш --------------------------\n";
        try (FileWriter fw = new FileWriter("prizes.txt", true)) {
            fw.write(s+'\n');
        }
        while (true) {
//            selectedListToys.removeIf(toy -> toy.countInRaffle == 0);
            if (!selectedListToys.isEmpty()) {
                System.out.println("В розыгрыше участвуют следующие игрушки:");
                showToysInRaffle(selectedListToys);
                System.out.println("--------------------------");
                getPrizeToy(selectedListToys);
                if (selectedListToys.isEmpty()) {
                    System.out.println("Для розыгрыша не осталось игрушек!");
                    break;
                } else {
                    System.out.println("Оставшиеся в розыгрыше игрушки:");
                    showToysInRaffle(selectedListToys);
                    System.out.println("Провести повторный розыгрыш выбранных игрушек? Напишите да или нет:");
                    String answer = in.nextLine();
                    if (answer.equalsIgnoreCase("нет")) {
                        break;
                    }
                }
            } else {
                System.out.println("Нет выбранных игрушек для розыгрыша! Повторите действия.");
                break;
            }
        }
    }

    private static void showToysInRaffle(ArrayList<Toy> selectedListToys) {
        for (Toy item : selectedListToys) {
            if (item.countInRaffle != 0) {
                item.printInfoForRaffle();
            }
        }
    }

    private static void getPrizeToy(ArrayList<Toy> selectedListToys) throws IOException {
        Random random = new Random();
        int num = random.nextInt(100);
//        int num = 95;    //для проверки
//        System.out.println(num);
        double sumFrequency = 0;
        for (Toy t : selectedListToys) {
            sumFrequency += t.frequency * 100;
        }
        double start = 0;
        for (Toy item : selectedListToys) {
            double toyFreq = (item.frequency * 10000) / sumFrequency;
            if (start < num && num <= start + toyFreq) {
                System.out.println("Ваш приз:");
                item.decreaseToyCount();
                item.decreaseToyCountInRaffle();
                System.out.println(item.name);
                System.out.println("Поздравляем!");
                System.out.println("--------------------------");

                String s = item.toStringForRaffle();
                try (FileWriter fw = new FileWriter("prizes.txt", true)) {
                    fw.write(s+'\n');
                }
            }
            start += toyFreq;
        }
        selectedListToys.removeIf(toy -> toy.countInRaffle == 0);
    }


    private static ArrayList<Toy> addToyToSelectedList(ArrayList<Toy> listToys) {
        ArrayList<Toy> selListToys = new ArrayList<>();
        Map<Integer, Integer> inputIdList = new HashMap<>();
        showAllToys(listToys);
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("--------------------------");
                System.out.println("Введите id игрушки, которую необходимо добавить в розыгрыш:");
                int inputId = Integer.parseInt(in.nextLine());
                if (isIdExists(inputId, listToys)) {
                    System.out.println("Какое количество этой игрушки необходимо добавить в розыгрыш?");
                    int inputCount = Integer.parseInt(in.nextLine());
                    for (Toy toy : listToys) {
                        if (toy.id == inputId) {
                            if (toy.isEnoughCount(inputCount)) {
                                inputIdList.put(inputId, inputCount);
                                if (inputCount == 0) {
                                    System.out.println("Игрушка не добавлена в розыгрыш!");
                                }
                                else {
                                    System.out.println("Игрушка добавлена в розыгрыш!");
                                }
                                System.out.println("--------------------------");
                            } else {
                                System.out.println("Вы ввели число больше имеющего количества данной игрушки!");
                                break;
                            }
                        }
                    }
                } else {
                    System.out.println("Нет игрушки с таким id. Проверьте корректность ввода.");
                    continue;
                }

                System.out.println("Хотите добавить еще игрушку в розыгрыш? Напишите да или нет:");
                String answer = in.nextLine();
                if (answer.equalsIgnoreCase("нет")) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Вы ввели не число!");
            }
        }
        for (var item : inputIdList.entrySet()) {
            for (Toy toy : listToys) {
                if (toy.id == item.getKey() && item.getValue() != 0) {
                    selListToys.add(toy);
                    toy.countInRaffle = item.getValue();
                }
            }
        }
        return selListToys;
    }

    private static boolean isIdExists(int inputId, ArrayList<Toy> listToys) {
        for (Toy toy : listToys) {
            if (toy.isToyExists(inputId)) {
                return true;
            }
        }
        return false;
    }

    private static void changeToyFrequency(ArrayList<Toy> listToys) {
        showAllToys(listToys);
        Scanner in = new Scanner(System.in);
        System.out.println("Введите id игрушки, у которой необходимо изменить частоту выпадения:");
        int inputId = Integer.parseInt(in.nextLine());
        if (isIdExists(inputId, listToys)) {
            for (Toy item : listToys) {
                if (item.id == inputId) {
                    System.out.println("Введите новую частоту выпадения игрушки:");
                    item.frequency = Double.parseDouble(in.nextLine());
                    System.out.println("Частота выпадения игрушки изменена:");
                    item.printInfo();
                }
            }
        } else {
            System.out.println("Нет игрушки с таким id");
        }
    }

    private static void showAllToys(ArrayList<Toy> listToys) {
        System.out.println("Все игрушки в наличии:");
        for (Toy item : listToys) {
            item.printInfo();
        }
    }

    private static void addNewToy(ArrayList<Toy> listToys) throws Exception {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите название игрушки:");
        String name = in.nextLine();
        System.out.println("Введите количество данной игрушки:");
        int count = Integer.parseInt(in.nextLine());
        System.out.println("Введите частоту выпадения данной игрушки в формате 0.70:");
        double frequency = Double.parseDouble(in.nextLine());
        int id = getIdToy();
        Toy t = new Toy(id, name, count, frequency);
        listToys.add(t);
        System.out.println("Игрушка добавлена в ассортимент!");
        showAllToys(listToys);
    }

    private static int getIdToy() throws Exception {
        File fr = new File("E:\\GeekBrains\\8.Java\\Control_work_Toy_shop\\next_id.txt");
        FileReader is = new FileReader(fr);
        BufferedReader br = new BufferedReader(is);

        int id = Integer.parseInt("" + br.readLine());
        int newId = id + 1;
        String s = Integer.toString(newId);
        try (FileWriter fw = new FileWriter("next_id.txt", false)) {
            fw.write(s);
        }
        return id;
    }
}
