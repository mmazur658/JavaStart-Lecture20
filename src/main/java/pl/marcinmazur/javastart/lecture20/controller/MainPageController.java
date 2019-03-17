package pl.marcinmazur.javastart.lecture20.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.marcinmazur.javastart.lecture20.entity.Product;

@Controller
public class MainPageController {

	@GetMapping("/")
	public String showIndex() {
		return "index";
	}

	@PostMapping("/addProduct")
	public String addProduct(HttpServletRequest request, @RequestParam(required = false) String name,
			@RequestParam(required = false) String price, Model theModel) {

		double priceD = 0;
		String messageText = null;
		String messageStatus = null;

		HttpSession session = request.getSession();
		List<Product> products = getProductList(session);

		if (name != null && price != null) {

			try {
				name = name.replaceAll(",", ".");
				priceD = Double.parseDouble(price);
				Product product = new Product(name, priceD);
				products.add(product);

				messageText = "Produkt został dodany.";
				messageStatus = "success";

			} catch (NumberFormatException exception) {
				messageText = "Niepriawidłowy format danych, nie można dodać produktu.";
				messageStatus = "danger";
			}

		} else {
			messageText = "Pola nazwa i cena nie mogą być puste.";
			messageStatus = "danger";
		}

		theModel.addAttribute("messageText", messageText);
		theModel.addAttribute("messageStatus", messageStatus);
		session.setAttribute("products", products);

		return "index";
	}

	@GetMapping("/lista")
	public String showList(HttpSession session, Model theModel) {

		List<Product> products = getProductList(session);
		String sum = getSumOfProductsValues(products);

		theModel.addAttribute("products", products);
		theModel.addAttribute("sum", sum);

		return "products-list";
	}

	@GetMapping("/tabela")
	public String showTable(HttpSession session, Model theModel) {

		List<Product> products = getProductList(session);
		String sum = getSumOfProductsValues(products);

		theModel.addAttribute("products", products);
		theModel.addAttribute("sum", sum);

		return "products-table";
	}

	@SuppressWarnings("unchecked")
	private List<Product> getProductList(HttpSession session) {

		List<Product> products = (List<Product>) session.getAttribute("products");

		if (products == null)
			products = new ArrayList<>();
		
		return products;

	}

	private String getSumOfProductsValues(List<Product> products) {

		double sum = 0;

		for (Product product : products)
			sum += product.getPrice();
		
		String sumS = String.format("%.2f", sum);

		return sumS;
	}

}
