package edu.java.lab6;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author Garifullin Marat
 * @version 1.6
 */
public class Shop {
	private JFrame shop;
	private DefaultTableModel sellersModel, productsModel;
	private JButton save, // Сохранить
			open, // Открыть файл
			add, // Добавить запись
			delete, // Удалить запись
			edit, // Изменить запись
			info, // Информация о магазине
			tableSearchButton; // Поиск записей
	private JToolBar toolBar; // Панель инструментов
	private JScrollPane sellersScroll, // Прокрутка таблицы продавцов
			productsScroll; // Прокрутка таблицы продуктов
	private JTable sellersTable, // Таблица продацов
			productsTable; // Таблица товаров
	private JLabel tableNameLabel, tableSearchLabel;
	private JComboBox tableNameBox, tableSearchSellersBox, tableSearchProductsBox;
	private JTextField tableSearchField; // Поисковое поле
	private Box tableContainer, // Контейнер для таблицы и прилегающих компонентов
			tableNameContainer, // Контейнер для выбора таблицы
			tableSearchContainer; // Контейнер для элементов поиска по таблице

	public void show() {
		// Создание окна
		shop = new JFrame("Магазин");
		shop.setSize(500, 300);
		shop.setLocation(100, 100);
		shop.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Создание кнопок
		save = new JButton(new ImageIcon("./icons/save.png"));
		open = new JButton(new ImageIcon("./icons/open.png"));
		add = new JButton(new ImageIcon("./icons/add.png"));
		delete = new JButton(new ImageIcon("./icons/delete.png"));
		edit = new JButton(new ImageIcon("./icons/edit.png"));
		info = new JButton(new ImageIcon("./icons/info.png"));

		// Настройка подсказок для кнопок
		save.setToolTipText("Сохранить");
		open.setToolTipText("Открыть файл");
		add.setToolTipText("Добавить запись");
		delete.setToolTipText("Удалить запись");
		edit.setToolTipText("Изменить запись");
		info.setToolTipText("О магазине");

		// Добавление кнопок на панель инструментов
		toolBar = new JToolBar("Панель инструментов");
		toolBar.add(save);
		toolBar.add(open);
		toolBar.add(add);
		toolBar.add(delete);
		toolBar.add(edit);
		toolBar.add(info);

		// Размещение панели инструментов
		shop.setLayout(new BorderLayout());
		shop.add(toolBar, BorderLayout.NORTH);

		// Создание панели выбора таблицы
		tableNameContainer = Box.createHorizontalBox();
		tableNameLabel = new JLabel("Таблица:");
		tableNameBox = new JComboBox(new String[] { "Продавцы", "Товары" });
		tableNameLabel.setLabelFor(tableNameBox);
		tableNameBox.setMaximumSize(new Dimension(90, 25));
		tableNameContainer.add(tableNameLabel);
		tableNameContainer.add(Box.createHorizontalStrut(6));
		tableNameContainer.add(tableNameBox);
		tableNameContainer.add(Box.createHorizontalGlue());

		// Создание таблицы с данными продавцов
		String sellersColumn[] = { "ФИО", "Дата рождения", "Зарплата" };
		String sellersData[][] = { { "Гарифуллин Марат Рафаилович", "13.02.2002", "0" },
				{ "Петров Сергей Олегович", "12.12.1999", "30" } };
		sellersModel = new DefaultTableModel(sellersData, sellersColumn);
		sellersTable = new JTable(sellersModel);
		sellersScroll = new JScrollPane(sellersTable);

		// Создание таблицы с данными товаров
		String productsColumn[] = { "Название товара", "Цена", "Количество" };
		String productsData[][] = { { "Sea of thieves", "3000", "3" }, { "Vampire Survivors", "81", "2000" } };
		productsModel = new DefaultTableModel(productsData, productsColumn);
		productsTable = new JTable(productsModel);
		productsScroll = new JScrollPane(productsTable);
		productsScroll.setVisible(false);

		// Создание панели поиска по таблице
		tableSearchContainer = Box.createHorizontalBox();
		tableSearchLabel = new JLabel("Поиск по:");
		tableSearchSellersBox = new JComboBox(new String[] { "ФИО", "Дата рождения", "Зарплата" });
		tableSearchProductsBox = new JComboBox(new String[] { "Название товара", "Цена", "Количество" });
		tableSearchProductsBox.setVisible(false);
		tableSearchField = new JTextField("Поиск");
		tableSearchButton = new JButton("Поиск");
		tableSearchButton.setFocusPainted(false);
		tableSearchContainer.add(tableSearchLabel);
		tableSearchContainer.add(Box.createHorizontalStrut(6));
		tableSearchContainer.add(tableSearchSellersBox);
		tableSearchContainer.add(tableSearchProductsBox);
		tableSearchContainer.add(Box.createHorizontalStrut(6));
		tableSearchContainer.add(tableSearchField);
		tableSearchContainer.add(Box.createHorizontalStrut(6));
		tableSearchContainer.add(tableSearchButton);

		// Размещение таблицы и прилегающих компонентов
		tableContainer = Box.createVerticalBox();
		tableContainer.add(tableNameContainer);
		tableContainer.add(sellersScroll);
		tableContainer.add(productsScroll);
		tableContainer.add(tableSearchContainer);
		shop.add(tableContainer, BorderLayout.CENTER);

		// Добавление слушателей
		addListeners();

		// Визуализация экранной формы
		shop.setVisible(true);
	}

	private void addListeners() {
		// Добавление слушателей
		save.addActionListener(new FirstAction());
		open.addActionListener(new FirstAction());
		add.addActionListener(new FirstAction());
		delete.addActionListener(new FirstAction());
		edit.addActionListener(new FirstAction());
		info.addActionListener(new FirstAction());
		tableNameBox.addActionListener(new FirstAction());
		tableSearchSellersBox.addActionListener(new FirstAction());
	}

	/**
	 * @param arr - массив для сортировки по убыванию
	 */
	private void sort(int[] arr) {
		int tmp;
		int len = arr.length;
		// Сортировка массива
		for (int i = 0; i < len; ++i) {
			for (int j = 0; j < len - i - 1; ++j) {
				if (arr[j] < arr[j + 1]) {
					tmp = arr[j];
					arr[j] = arr[j + 1];
					arr[j + 1] = tmp;
				}
			}
		}
	}

	// Блоки прослушивания событий
	public class FirstAction implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == save) { // Сохранение в файл
				try {
					DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					Document doc = builder.newDocument();
					int currentTable = tableNameBox.getSelectedIndex();
					if (currentTable == 0) {
						// Создание корневого элемента sellerlist и добавление его в документ
						Node sellerlist = doc.createElement("sellerlist");
						doc.appendChild(sellerlist);
						// Создание дочерних элементов seller и присвоение значений атрибутам
						for (int i = 0; i < sellersModel.getRowCount(); ++i) {
							Element seller = doc.createElement("seller");
							sellerlist.appendChild(seller);
							seller.setAttribute("fio", (String) sellersModel.getValueAt(i, 0));
							seller.setAttribute("birthDate", (String) sellersModel.getValueAt(i, 1));
							seller.setAttribute("salary", (String) sellersModel.getValueAt(i, 2));
						}
						try {
							// Создание преобразователя документа
							Transformer trans = TransformerFactory.newInstance().newTransformer();
							// Создание файла с именем sellers.xml для записи документа
							FileWriter fw = new FileWriter("sellers.xml");
							// Запись документа в файл
							trans.transform(new DOMSource(doc), new StreamResult(fw));
						} catch (TransformerConfigurationException e) {
							e.printStackTrace();
						} catch (TransformerException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (currentTable == 1) {
						// Создание корневого элемента productlist и добавление его в документ
						Node productlist = doc.createElement("productlist");
						doc.appendChild(productlist);
						// Создание дочерних элементов product и присвоение значений атрибутам
						for (int i = 0; i < productsModel.getRowCount(); ++i) {
							Element product = doc.createElement("product");
							productlist.appendChild(product);
							product.setAttribute("name", (String) productsModel.getValueAt(i, 0));
							product.setAttribute("price", (String) productsModel.getValueAt(i, 1));
							product.setAttribute("num", (String) productsModel.getValueAt(i, 2));
						}
						try {
							// Создание преобразователя документа
							Transformer trans = TransformerFactory.newInstance().newTransformer();
							// Создание файла с именем sellers.xml для записи документа
							FileWriter fw = new FileWriter("products.xml");
							// Запись документа в файл
							trans.transform(new DOMSource(doc), new StreamResult(fw));
						} catch (TransformerConfigurationException e) {
							e.printStackTrace();
						} catch (TransformerException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (ParserConfigurationException ex) {
					ex.printStackTrace();
				}
			} else if (event.getSource() == open) { // Загрузка из файла
				try {
					// Создание парсера документа
					DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
					int currentTable = tableNameBox.getSelectedIndex();
					if (currentTable == 0) {
						// Чтение документа из файла
						Document doc = dBuilder.parse(new File("sellers.xml"));
						// Нормализация документа
						doc.getDocumentElement().normalize();
						// Получение списка элементов с именем seller
						NodeList nlSellers = doc.getElementsByTagName("seller");
						int rows = sellersModel.getRowCount();
						for (int i = 0; i < rows; i++) sellersModel.removeRow(0); // Очистка таблицы
						// Цикл просмотра списка элементов и запись данных в таблицу
						for(int i = 0; i < nlSellers.getLength(); ++i) {
							// Выбор очередного элемента списка
							Node elem = nlSellers.item(i);
							// Получение списка атрибутов элемента
							NamedNodeMap attrs = elem.getAttributes();
							// Чтение атрибутов элемента
							String fio = attrs.getNamedItem("fio").getNodeValue();
							String birthDate = attrs.getNamedItem("birthDate").getNodeValue();
							String salary = attrs.getNamedItem("salary").getNodeValue();
							// Запись данных в таблицу
							sellersModel.addRow(new String[]{fio, birthDate, salary});
						}
					} else if (currentTable == 1) {
						// Чтение документа из файла
						Document doc = dBuilder.parse(new File("products.xml"));
						// Нормализация документа
						doc.getDocumentElement().normalize();
						// Получение списка элементов с именем seller
						NodeList nlProducts = doc.getElementsByTagName("product");
						int rows = productsModel.getRowCount();
						for (int i = 0; i < rows; i++) productsModel.removeRow(0); // Очистка таблицы
						// Цикл просмотра списка элементов и запись данных в таблицу
						for(int i = 0; i < nlProducts.getLength(); ++i) {
							// Выбор очередного элемента списка
							Node elem = nlProducts.item(i);
							// Получение списка атрибутов элемента
							NamedNodeMap attrs = elem.getAttributes();
							// Чтение атрибутов элемента
							String name = attrs.getNamedItem("name").getNodeValue();
							String price = attrs.getNamedItem("price").getNodeValue();
							String num = attrs.getNamedItem("num").getNodeValue();
							// Запись данных в таблицу
							productsModel.addRow(new String[]{name, price, num});
						}
					}
				} catch (ParserConfigurationException ex) {
					ex.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (event.getSource() == add) { // Добавление в таблицу
				int currentTable = tableNameBox.getSelectedIndex();
				if (currentTable == 0) {
					SellerWindow seller = new SellerWindow();
				} else if (currentTable == 1) {
					ProductWindow product = new ProductWindow();
				}
			} else if (event.getSource() == delete) { // Удаление из таблицы
				int currentTable = tableNameBox.getSelectedIndex();
				if (currentTable == 0) {
					int[] selectedRows = sellersTable.getSelectedRows();
					if (selectedRows.length != 0) {
						sort(selectedRows);
						for (int i : selectedRows) {
							sellersModel.removeRow(i);
						}
					} else {
						JOptionPane.showMessageDialog(shop, "Вы не выбрали строку для удаления!");
					}
				} else if (currentTable == 1) {
					int[] selectedRows = productsTable.getSelectedRows();
					if (selectedRows.length != 0) {
						sort(selectedRows);
						for (int i : selectedRows) {
							productsModel.removeRow(i);
						}
					} else {
						JOptionPane.showMessageDialog(shop, "Вы не выбрали строку для удаления!");
					}
				}
			} else if (event.getSource() == edit) { // Изменение записи

			} else if (event.getSource() == info) { // Вывод информации о магазине

			} else if (event.getSource() == tableNameBox) { // Смена таблиц
				int selectedIndex = tableNameBox.getSelectedIndex(); // 0 - Продавцы, 1 - Товары
				if (selectedIndex == 0) {
					sellersScroll.setVisible(true);
					productsScroll.setVisible(false);
					tableSearchSellersBox.setVisible(true);
					tableSearchProductsBox.setVisible(false);
				} else if (selectedIndex == 1) {
					sellersScroll.setVisible(false);
					productsScroll.setVisible(true);
					tableSearchSellersBox.setVisible(false);
					tableSearchProductsBox.setVisible(true);
				}
				shop.setVisible(true);
			} else if (event.getSource() == tableSearchSellersBox) {
				System.out.println(tableSearchSellersBox.getSelectedIndex());
			}
		}
	}

	// Окно добавления продавца
	public class SellerWindow extends JFrame {
		Container mainBox;
		JPanel panel1, panel2, panel3;
		JLabel FIOLabel, birthDateLabel, salaryLabel;
		JTextField FIOField, birthDateField, salaryField;
		JButton addSeller;
		SpringLayout layout, layout1, layout2, layout3;

		public SellerWindow() {
			this.setTitle("Добавление продавца");
			this.setBounds(200, 100, 500, 220);
			this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			this.setResizable(false);

			// Создание надписей
			FIOLabel = new JLabel("Введите ФИО:");
			birthDateLabel = new JLabel("Введите дату рождения:");
			salaryLabel = new JLabel("Введите зарплату:");

			// Создание полей для ввода
			FIOField = new JTextField();
			birthDateField = new JTextField();
			salaryField = new JTextField();

			// Создание кнопки добавления
			addSeller = new JButton("Добавить");

			// Размещение элементов
			mainBox = this.getContentPane();
			layout = new SpringLayout();
			layout1 = new SpringLayout();
			layout2 = new SpringLayout();
			layout3 = new SpringLayout();
			panel1 = new JPanel();
			panel2 = new JPanel();
			panel3 = new JPanel();
			mainBox.setLayout(layout);
			panel1.setLayout(layout1);
			panel2.setLayout(layout2);
			panel3.setLayout(layout3);
			panel1.add(FIOLabel);
			panel1.add(FIOField);
			panel2.add(birthDateLabel);
			panel2.add(birthDateField);
			panel3.add(salaryLabel);
			panel3.add(salaryField);
			mainBox.add(panel1);
			mainBox.add(panel2);
			mainBox.add(panel3);
			mainBox.add(addSeller);
			// Размещение panel1
			layout.putConstraint(SpringLayout.WEST, panel1, 5, SpringLayout.WEST, mainBox);
			layout.putConstraint(SpringLayout.EAST, panel1, -5, SpringLayout.EAST, mainBox);
			layout.putConstraint(SpringLayout.NORTH, panel1, 10, SpringLayout.NORTH, mainBox);
			layout.putConstraint(SpringLayout.SOUTH, panel1, 35, SpringLayout.NORTH, panel1);
			// Размещение panel2
			layout.putConstraint(SpringLayout.NORTH, panel2, 10, SpringLayout.SOUTH, panel1);
			layout.putConstraint(SpringLayout.SOUTH, panel2, 35, SpringLayout.NORTH, panel2);
			layout.putConstraint(SpringLayout.WEST, panel2, 5, SpringLayout.WEST, mainBox);
			layout.putConstraint(SpringLayout.EAST, panel2, -5, SpringLayout.EAST, mainBox);
			// Размещение panel3
			layout.putConstraint(SpringLayout.NORTH, panel3, 10, SpringLayout.SOUTH, panel2);
			layout.putConstraint(SpringLayout.SOUTH, panel3, 35, SpringLayout.NORTH, panel3);
			layout.putConstraint(SpringLayout.WEST, panel3, 5, SpringLayout.WEST, mainBox);
			layout.putConstraint(SpringLayout.EAST, panel3, -5, SpringLayout.EAST, mainBox);
			// Размещение кнопки addSeller
			layout.putConstraint(SpringLayout.EAST, addSeller, -175, SpringLayout.EAST, mainBox);
			layout.putConstraint(SpringLayout.SOUTH, addSeller, -10, SpringLayout.SOUTH, mainBox);
			layout.putConstraint(SpringLayout.WEST, addSeller, 175, SpringLayout.WEST, mainBox);
			// Размещение внутри panel1
			layout1.putConstraint(SpringLayout.WEST, FIOLabel, 5, SpringLayout.WEST, panel1);
			layout1.putConstraint(SpringLayout.NORTH, FIOLabel, 5, SpringLayout.NORTH, panel1);
			layout1.putConstraint(SpringLayout.SOUTH, FIOLabel, -5, SpringLayout.SOUTH, panel1);
			layout1.putConstraint(SpringLayout.NORTH, FIOField, 5, SpringLayout.NORTH, panel1);
			layout1.putConstraint(SpringLayout.EAST, FIOField, -30, SpringLayout.EAST, panel1);
			layout1.putConstraint(SpringLayout.SOUTH, FIOField, -5, SpringLayout.SOUTH, panel1);
			layout1.putConstraint(SpringLayout.WEST, FIOField, 170, SpringLayout.WEST, panel1);
			// Размещение внутри panel2
			layout2.putConstraint(SpringLayout.WEST, birthDateLabel, 5, SpringLayout.WEST, panel2);
			layout2.putConstraint(SpringLayout.NORTH, birthDateLabel, 5, SpringLayout.NORTH, panel2);
			layout2.putConstraint(SpringLayout.SOUTH, birthDateLabel, -5, SpringLayout.SOUTH, panel2);
			layout2.putConstraint(SpringLayout.NORTH, birthDateField, 5, SpringLayout.NORTH, panel2);
			layout2.putConstraint(SpringLayout.EAST, birthDateField, -30, SpringLayout.EAST, panel2);
			layout2.putConstraint(SpringLayout.SOUTH, birthDateField, -5, SpringLayout.SOUTH, panel2);
			layout2.putConstraint(SpringLayout.WEST, birthDateField, 170, SpringLayout.WEST, panel2);
			// Размещение внутри panel3
			layout3.putConstraint(SpringLayout.WEST, salaryLabel, 5, SpringLayout.WEST, panel3);
			layout3.putConstraint(SpringLayout.NORTH, salaryLabel, 5, SpringLayout.NORTH, panel3);
			layout3.putConstraint(SpringLayout.SOUTH, salaryLabel, -5, SpringLayout.SOUTH, panel3);
			layout3.putConstraint(SpringLayout.NORTH, salaryField, 5, SpringLayout.NORTH, panel3);
			layout3.putConstraint(SpringLayout.EAST, salaryField, -30, SpringLayout.EAST, panel3);
			layout3.putConstraint(SpringLayout.SOUTH, salaryField, -5, SpringLayout.SOUTH, panel3);
			layout3.putConstraint(SpringLayout.WEST, salaryField, 170, SpringLayout.WEST, panel3);

			// Добавление слушателей
			addSeller.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try {
						checkName(FIOField);
						checkBirthDate(birthDateField);
						checkSalary(salaryField);
						String sellerRow[] = new String[3];
						sellerRow[0] = FIOField.getText();
						sellerRow[1] = birthDateField.getText();
						sellerRow[2] = salaryField.getText();
						sellersModel.addRow(sellerRow);
						FIOField.setText("");
						birthDateField.setText("");
						salaryField.setText("");
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(shop, ex.toString());
					} catch (HaveDigit ex) {
						JOptionPane.showMessageDialog(shop, ex.toString());
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(shop, ex.toString());
					}
				}
			});
			this.setVisible(true);
		}

		// Проверка ФИО
		private void checkName(JTextField textField) throws HaveDigit, NullPointerException {
			String name = textField.getText();
			if (name.length() == 0)
				throw new NullPointerException();
			for (int i = 0; i < name.length(); ++i) {
				if (0 <= name.charAt(i) - '0' && name.charAt(i) - '0' <= 9) {
					throw new HaveDigit();
				}
			}
		}

		// Проверка даты рождения
		private void checkBirthDate(JTextField textField) throws NullPointerException {
			String birthDate = textField.getText();
			if (birthDate.length() == 0)
				throw new NullPointerException();
		}

		// Проверка зарплаты
		private void checkSalary(JTextField textField) throws NullPointerException, NumberFormatException {
			String salary = textField.getText();
			if (salary.length() == 0)
				throw new NullPointerException();
			for (int i = 0; i < salary.length(); ++i) {
				if (salary.charAt(i) - '0' < 0 || salary.charAt(i) - '0' > 9) {
					throw new NumberFormatException();
				}
			}
		}
	}

	// Окно добавления товара
	public class ProductWindow extends JFrame {
		Container mainBox;
		JPanel panel1, panel2, panel3;
		JLabel productNameLabel, priceLabel, numLabel;
		JTextField productNameField, priceField, numField;
		JButton addProduct;
		SpringLayout layout, layout1, layout2, layout3;

		public ProductWindow() {
			this.setTitle("Добавление товара");
			this.setBounds(200, 100, 500, 220);
			this.setDefaultCloseOperation(HIDE_ON_CLOSE);
			this.setResizable(false);

			// Создание надписей
			productNameLabel = new JLabel("Введите название:");
			priceLabel = new JLabel("Введите цену:");
			numLabel = new JLabel("Введите количество:");

			// Создание полей для ввода
			productNameField = new JTextField();
			priceField = new JTextField();
			numField = new JTextField();

			// Создание кнопки добавления
			addProduct = new JButton("Добавить");

			// Размещение элементов
			mainBox = this.getContentPane();
			layout = new SpringLayout();
			layout1 = new SpringLayout();
			layout2 = new SpringLayout();
			layout3 = new SpringLayout();
			panel1 = new JPanel();
			panel2 = new JPanel();
			panel3 = new JPanel();
			mainBox.setLayout(layout);
			panel1.setLayout(layout1);
			panel2.setLayout(layout2);
			panel3.setLayout(layout3);
			panel1.add(productNameLabel);
			panel1.add(productNameField);
			panel2.add(priceLabel);
			panel2.add(priceField);
			panel3.add(numLabel);
			panel3.add(numField);
			mainBox.add(panel1);
			mainBox.add(panel2);
			mainBox.add(panel3);
			mainBox.add(addProduct);
			// Размещение panel1
			layout.putConstraint(SpringLayout.WEST, panel1, 5, SpringLayout.WEST, mainBox);
			layout.putConstraint(SpringLayout.EAST, panel1, -5, SpringLayout.EAST, mainBox);
			layout.putConstraint(SpringLayout.NORTH, panel1, 10, SpringLayout.NORTH, mainBox);
			layout.putConstraint(SpringLayout.SOUTH, panel1, 35, SpringLayout.NORTH, panel1);
			// Размещение panel2
			layout.putConstraint(SpringLayout.NORTH, panel2, 10, SpringLayout.SOUTH, panel1);
			layout.putConstraint(SpringLayout.SOUTH, panel2, 35, SpringLayout.NORTH, panel2);
			layout.putConstraint(SpringLayout.WEST, panel2, 5, SpringLayout.WEST, mainBox);
			layout.putConstraint(SpringLayout.EAST, panel2, -5, SpringLayout.EAST, mainBox);
			// Размещение panel3
			layout.putConstraint(SpringLayout.NORTH, panel3, 10, SpringLayout.SOUTH, panel2);
			layout.putConstraint(SpringLayout.SOUTH, panel3, 35, SpringLayout.NORTH, panel3);
			layout.putConstraint(SpringLayout.WEST, panel3, 5, SpringLayout.WEST, mainBox);
			layout.putConstraint(SpringLayout.EAST, panel3, -5, SpringLayout.EAST, mainBox);
			// Размещение кнопки addSeller
			layout.putConstraint(SpringLayout.EAST, addProduct, -175, SpringLayout.EAST, mainBox);
			layout.putConstraint(SpringLayout.SOUTH, addProduct, -10, SpringLayout.SOUTH, mainBox);
			layout.putConstraint(SpringLayout.WEST, addProduct, 175, SpringLayout.WEST, mainBox);
			// Размещение внутри panel1
			layout1.putConstraint(SpringLayout.WEST, productNameLabel, 5, SpringLayout.WEST, panel1);
			layout1.putConstraint(SpringLayout.NORTH, productNameLabel, 5, SpringLayout.NORTH, panel1);
			layout1.putConstraint(SpringLayout.SOUTH, productNameLabel, -5, SpringLayout.SOUTH, panel1);
			layout1.putConstraint(SpringLayout.NORTH, productNameField, 5, SpringLayout.NORTH, panel1);
			layout1.putConstraint(SpringLayout.EAST, productNameField, -30, SpringLayout.EAST, panel1);
			layout1.putConstraint(SpringLayout.SOUTH, productNameField, -5, SpringLayout.SOUTH, panel1);
			layout1.putConstraint(SpringLayout.WEST, productNameField, 170, SpringLayout.WEST, panel1);
			// Размещение внутри panel2
			layout2.putConstraint(SpringLayout.WEST, priceLabel, 5, SpringLayout.WEST, panel2);
			layout2.putConstraint(SpringLayout.NORTH, priceLabel, 5, SpringLayout.NORTH, panel2);
			layout2.putConstraint(SpringLayout.SOUTH, priceLabel, -5, SpringLayout.SOUTH, panel2);
			layout2.putConstraint(SpringLayout.NORTH, priceField, 5, SpringLayout.NORTH, panel2);
			layout2.putConstraint(SpringLayout.EAST, priceField, -30, SpringLayout.EAST, panel2);
			layout2.putConstraint(SpringLayout.SOUTH, priceField, -5, SpringLayout.SOUTH, panel2);
			layout2.putConstraint(SpringLayout.WEST, priceField, 170, SpringLayout.WEST, panel2);
			// Размещение внутри panel3
			layout3.putConstraint(SpringLayout.WEST, numLabel, 5, SpringLayout.WEST, panel3);
			layout3.putConstraint(SpringLayout.NORTH, numLabel, 5, SpringLayout.NORTH, panel3);
			layout3.putConstraint(SpringLayout.SOUTH, numLabel, -5, SpringLayout.SOUTH, panel3);
			layout3.putConstraint(SpringLayout.NORTH, numField, 5, SpringLayout.NORTH, panel3);
			layout3.putConstraint(SpringLayout.EAST, numField, -30, SpringLayout.EAST, panel3);
			layout3.putConstraint(SpringLayout.SOUTH, numField, -5, SpringLayout.SOUTH, panel3);
			layout3.putConstraint(SpringLayout.WEST, numField, 170, SpringLayout.WEST, panel3);

			// Добавление слушателей
			addProduct.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					try {
						checkProductName(productNameField);
						checkPrice(priceField);
						checkProductNum(numField);
						String productRow[] = new String[3];
						productRow[0] = productNameField.getText();
						productRow[1] = priceField.getText();
						productRow[2] = numField.getText();
						productsModel.addRow(productRow);
						productNameField.setText("");
						priceField.setText("");
						numField.setText("");
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(shop, ex.toString());
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(shop, ex.toString());
					}
				}
			});
			this.setVisible(true);
		}

		// Проверка названия товара
		private void checkProductName(JTextField textField) throws NullPointerException {
			String productName = textField.getText();
			if (productName.length() == 0)
				throw new NullPointerException();
		}

		// Проверка цены товара
		private void checkPrice(JTextField textField) throws NullPointerException, NumberFormatException {
			String price = textField.getText();
			if (price.length() == 0)
				throw new NullPointerException();
			for (int i = 0; i < price.length(); ++i) {
				if (price.charAt(i) - '0' < 0 || price.charAt(i) - '0' > 9) {
					throw new NumberFormatException();
				}
			}
		}

		// Проверка количества товара
		private void checkProductNum(JTextField textField) throws NullPointerException, NumberFormatException {
			String productNum = textField.getText();
			if (productNum.length() == 0)
				throw new NullPointerException();
			for (int i = 0; i < productNum.length(); ++i) {
				if (productNum.charAt(i) - '0' < 0 || productNum.charAt(i) - '0' > 9) {
					throw new NumberFormatException();
				}
			}
		}
	}

	// Классы исключений
	// Класс исключения, возникающего, когда в строке содержатся цифры
	private class HaveDigit extends Exception {
		public HaveDigit() {
			super("Введённая строка содержит цифры");
		}
	}

	/**
	 * @param args - вводимая строка
	 */
	public static void main(String[] args) {
		Shop shop = new Shop();
		shop.show();
	}
}
