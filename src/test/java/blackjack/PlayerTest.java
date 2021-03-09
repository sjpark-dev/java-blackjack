package blackjack;

import blackjack.domain.Card;
import blackjack.domain.Participant;
import blackjack.domain.Player;
import blackjack.utils.CardDeck;
import blackjack.utils.FixedCardDeck;
import blackjack.utils.IllegalNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PlayerTest {
    @Test
    void create() {
        Participant player = new Player("john", new FixedCardDeck());
        assertThat(player.getName()).isEqualTo("john");
    }

    @Test
    void create2() {
        Participant player = new Player("sarah", new FixedCardDeck());
        assertThat(player.getName()).isEqualTo("sarah");
    }

    @Test
    @DisplayName("플레이어가 초기에 카드 두장을 갖고 있는지 확인")
    void create3() {
        final CardDeck cardDeck = new FixedCardDeck();
        List<Card> cards = cardDeck.initCards();
        Participant player = new Player("sarah", cards);

        List<Card> playerCards = player.getUnmodifiableCards();
        assertThat(playerCards).contains(Card.from("A클로버"), Card.from("2클로버"));
    }

    @Test
    @DisplayName("이름이 빈문자열이면 예외 발생 확인")
    void create4() {
        assertThatThrownBy(() -> new Player("", new FixedCardDeck()))
                .isInstanceOf(IllegalNameException.class)
                .hasMessage("이름이 null이거나 빈 문자열일 수 없습니다.");
    }

    @Test
    @DisplayName("이름이 null이면 예외 발생 확인")
    void create5() {
        assertThatThrownBy(() -> new Player(null, new FixedCardDeck()))
                .isInstanceOf(IllegalNameException.class)
                .hasMessage("이름이 null이거나 빈 문자열일 수 없습니다.");
    }

    @Test
    @DisplayName("이름에 한글이 섞여있을 때 예외 발생 확인")
    void create6() {
        assertThatThrownBy(() -> new Player("john한글", new FixedCardDeck()))
                .isInstanceOf(IllegalNameException.class)
                .hasMessage("이름은 알파벳 대소문자로 이루어져야 합니다.");
    }

    @Test
    @DisplayName("이름애 숫자가 섞여있을 때 예외 발생 확인")
    void create7() {
        assertThatThrownBy(() -> new Player("john123", new FixedCardDeck()))
                .isInstanceOf(IllegalNameException.class)
                .hasMessage("이름은 알파벳 대소문자로 이루어져야 합니다.");
    }


    @Test
    @DisplayName("플레이어에게 카드 추가 지급")
    void add_card() {
        final CardDeck cardDeck = new FixedCardDeck();
        List<Card> cards = cardDeck.initCards();
        Participant player = new Player("sarah", cards);
        player.takeCard(cardDeck.pop());
        assertThat(player.getUnmodifiableCards()).contains(Card.from("A클로버"), Card.from("2클로버"), Card.from("3클로버"));
    }

    @Test
    @DisplayName("플레이어에게 지급된 카드 합계")
    void sum_cards() {
        final CardDeck cardDeck = new FixedCardDeck();
        List<Card> cards = cardDeck.initCards();
        Participant player = new Player("sarah", cards);
        int score = player.sumCards();
        assertThat(score).isEqualTo(3);
    }

    @Test
    @DisplayName("결과를 위한 플레이어에게 지급된 카드 합계")
    void sum_cards_for_result() {
        List<Card> cards = Arrays.asList(Card.from("A다이아몬드"), Card.from("6다이아몬드"));
        Participant player = new Player("john", cards);
        int score = player.sumCardsForResult();
        assertThat(score).isEqualTo(17);
    }

    @Test
    @DisplayName("Ace 4장인 경우 지지않는 최대 합계")
    void sum_cards_for_result1() {
        List<Card> cards = Arrays.asList(Card.from("A다이아몬드"),
                Card.from("A다이아몬드"));
        Participant player = new Player("john", cards);
        player.takeCard(Card.from("A다이아몬드"));
        player.takeCard(Card.from("A다이아몬드"));

        int score = player.sumCardsForResult();

        assertThat(score).isEqualTo(14);
    }
}
