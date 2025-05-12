import React from 'react';
import './App.css'; // import the css file to enable your styles.
// Import the updated GameState interface from game.ts
import { GameState, Cell } from './game';
import BoardCell from './Cell';

/**
 * Define the type of the props field for a React component
 */
interface Props { }

/**
 * Using generics to specify the type of props and state.
 * props and state is a special field in a React component.
 * React will keep track of the value of props and state.
 * Any time there's a change to their values, React will
 * automatically update (not fully re-render) the HTML needed.
 *
 * props and state are similar in the sense that they manage
 * the data of this component. A change to their values will
 * cause the view (HTML) to change accordingly.
 *
 * Usually, props is passed and changed by the parent component;
 * state is the internal value of the component and managed by
 * the component itself.
 */
// The state type is now the updated GameState interface
class App extends React.Component<Props, GameState> {
  private initialized: boolean = false;

  /**
   * @param props has type Props
   */
  constructor(props: Props) {
    super(props)
    /**
     * state has type GameState as specified in the class inheritance.
     * Initialize state with default values for new fields
     */
    this.state = { cells: [], currentPlayer: 'X', winner: undefined, isDraw: false } // Added initial values for new state properties
  }

  /**
   * Use arrow function, i.e., () => {} to create an async function,
   * otherwise, 'this' would become undefined in runtime. This is
   * just an issue of Javascript.
   */
  newGame = async () => {
    const response = await fetch('/newgame');
    // Assume the backend returns the full GameState object
    const json: GameState = await response.json();
    // Update the entire state with the new game state from the backend
    this.setState(json);
  }

  /**
   * play will generate an anonymous function that the component
   * can bind with.
   * @param x
   * @param y
   * @returns
   */
  play(x: number, y: number): React.MouseEventHandler {
    return async (e) => {
      // prevent the default behavior on clicking a link; otherwise, it will jump to a new page.
      e.preventDefault();

      // Only allow playing if there is no winner and no draw
      if (!this.state.winner && !this.state.isDraw) {
        const response = await fetch(`/play?x=${x}&y=${y}`);
        // Assume the backend returns the full GameState object
        const json: GameState = await response.json();
        // Update the entire state with the new game state from the backend
        this.setState(json);
      }
      // Optionally, provide feedback if trying to play after the game ends
      // else {
      //   console.log("Game is over!");
      // }
    }
  }

  // Added undoMove function
  undoMove = async () => {
    // Send a request to the backend's undo endpoint
    // Assume the backend processes undo and returns the previous game state
    const response = await fetch('/undo');
    // Assume the backend returns the full GameState object after undoing
    const json: GameState = await response.json();
    // Update the state with the game state received after undo
    this.setState(json);
  }


  createCell(cell: Cell, index: number): React.ReactNode {
    // The playable check now also considers if the game is already over
    if (cell.playable && !this.state.winner && !this.state.isDraw)
      /**
       * key is used for React when given a list of items. It
       * helps React to keep track of the list items and decide
       * which list item need to be updated.
       * @see https://reactjs.org/docs/lists-and-keys.html#keys
       */
      return (
        <div key={index}>
          <a href='/' onClick={this.play(cell.x, cell.y)}>
            <BoardCell cell={cell}></BoardCell>
          </a>
        </div>
      )
    else
      // If the cell is not playable or the game is over, render it without a clickable link
      return (
        <div key={index}><BoardCell cell={cell}></BoardCell></div>
      )
  }

  /**
   * This function will call after the HTML is rendered.
   * We update the initial state by creating a new game.
   * @see https://reactjs.org/docs/react-component.html#componentdidmount
   */
  componentDidMount(): void {
    /**
     * setState in DidMount() will cause it to render twice which may cause
     * this function to be invoked twice. Use initialized to avoid that.
     */
    if (!this.initialized) {
      this.newGame();
      this.initialized = true;
    }
  }

  /**
   * The only method you must define in a React.Component subclass.
   * @returns the React element via JSX.
   * @see https://reactjs.org/docs/react-component.html
   */
  render(): React.ReactNode {
    /**
     * We use JSX to define the template. An advantage of JSX is that you
     * can treat HTML elements as code.
     * @see https://reactjs.org/docs/introducing-jsx.html
     */
    return (
      <div>
        {/* Added instructions div */}
        <div id="instructions">
          {/* Conditional rendering based on game state */}
          {this.state.winner
            ? `Тоглогч ${this.state.winner} яллаа!` // Display winner
            : this.state.isDraw
              ? 'Тоглоом тэнцлээ!' // Display draw message
              : this.state.currentPlayer
                ? `Одоогийн ээлж: ${this.state.currentPlayer}` // Display current player
                : 'Шинэ тоглоом эхлүүлэхийг хүлээнэ үү...'} {/* Default message */}
        </div>

        <div id="board">
          {/* Mapping over cells to create board cells */}
          {this.state.cells.map((cell, i) => this.createCell(cell, i))}
        </div>

        <div id="bottombar">
          {/* New Game button */}
          <button onClick={this.newGame}>New Game</button>
          {/* Undo button - Added onClick handler */}
          <button onClick={this.undoMove}>Undo</button>
        </div>
      </div>
    );
  }
}

export default App;