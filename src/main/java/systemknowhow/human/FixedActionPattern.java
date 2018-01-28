/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systemknowhow.human;

/**
 * The term fixed action pattern (FAP), or modal action pattern, is sometimes
 * used in ethology to denote an instinctive behavioral sequence that is
 * relatively invariant within the species and almost inevitably runs to
 * completion.
 *
 * Fixed action patterns, or similar behaviour sequences, are produced by a
 * neural network known as the innate releasing mechanism in response to an
 * external sensory stimulus known as a sign stimulus or releaser. A fixed
 * action pattern is one of the few types of behaviors which was thought to be
 * "hard-wired" and instinctive.
 *
 * @author Maneesh
 */
public class FixedActionPattern {
    String instinct;//Key
    Action FixedAction;//Value
    int ActionCount;//To be used by other senses
    /**
     * The term "sign stimulus", or "releaser", is used to denote a simple
     * feature of a complex stimulus that can elicit a FAP. For example, the red
     * belly of a male stickleback elicits a head-down, attack behaviour in
     * other male sticklebacks. This same response can be elicited by artificial
     * models or objects that contain the sign stimulus of red, for example, a
     * red coloured card.
     *
     * The terms sign stimulus and releaser are sometimes used interchangeably,
     * however, they have different meanings. The term sign stimulus is used to
     * denote a feature of an animal's environment that elicits a particular
     * response. The term releaser is used for a stimulus that has evolved to
     * facilitate communication between conspecifics (animals of the same
     * species).
     */
    SignStimulus sign_stimulus;//Reason or environment
    Releaser releaser;//Common talking ground
    
}
