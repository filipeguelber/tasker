package controllers;

import java.util.List;

import models.Card;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * This controller is supposed to expose the Cards REST API.
 * 
 * @author Tiago Garcia
 * @see http://github.com/tiagorg
 */
public class Cards extends Controller {

	/**
	 * Retrieves all cards and return OK (200) with the cards as JSON.
	 * 
	 * @return the result
	 */
	public static Result getAll() {
		List<Card> cards = Card.all();
		return ok(Json.toJson(cards));
	}

	/**
	 * Searches for a card and return OK (200) with the card as JSON if
	 * successful, or NOT_FOUND (404) otherwise.
	 * 
	 * @param id
	 *           the card id
	 * @return the result
	 */
	public static Result get(Long id) {
		Result result = null;

		Card card = Card.byId(id);
		if (card != null) {
			result = ok(Json.toJson(card));
		} else {
			result = notFound();
		}
		return result;
	}

	/**
	 * Creates a card from the request body and return OK (200) if successful, or
	 * BAD_REQUEST (400) otherwise.
	 * 
	 * @return the result
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result create() {
		ObjectMapper mapper = new ObjectMapper();
		Result result = null;

		try {
			JsonNode jsonNode = request().body().asJson();
			Card card = mapper.readValue(jsonNode, Card.class);
			Card.create(card);
			result = ok(Json.toJson(card));
		} catch (Exception e) {
			result = badRequest();
		}
		return result;
	}

	/**
	 * Updates a card from the request body and return OK (200) with the card as
	 * JSON if successful, NO_CONTENT (204) if there was no change and NOT_FOUND
	 * (404) otherwise.
	 * 
	 * @param id
	 *           the card id
	 * @return the return
	 */
	@BodyParser.Of(BodyParser.Json.class)
	public static Result update(Long id) {
		ObjectMapper mapper = new ObjectMapper();
		Result result = null;

		try {
			JsonNode jsonNode = request().body().asJson();
			Card originalCard = Card.byId(id);
			Card modifiedCard = mapper.readValue(jsonNode, Card.class);

			if (originalCard.getStatus().equals(modifiedCard.getStatus()) == false) {
				originalCard.setStatus(modifiedCard.getStatus());
				Card.update(originalCard);
				result = ok(Json.toJson(originalCard));
			} else {
				result = noContent();
			}
		} catch (Exception e) {
			result = badRequest();
		}
		return result;
	}

	/**
	 * Deletes a card and return OK (200) if successful, or NOT_FOUND (404)
	 * otherwise.
	 * 
	 * @param id
	 *           the card id
	 * @return the return
	 */
	public static Result delete(Long id) {
		Result result = null;

		try {
			Card.delete(id);
			result = ok();
		} catch (Exception e) {
			result = badRequest();
		}
		return result;
	}

}