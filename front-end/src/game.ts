// game.ts файл дотор (жишээ байдлаар)
export interface Cell {
  x: number;
  y: number;
  content: string; // 'X', 'O', or ''
  playable: boolean;
}

export interface GameState {
  cells: Cell[];
  currentPlayer?: string; // 'X' or 'O'
  winner?: string; // 'X', 'O', or undefined if no winner yet
  isDraw?: boolean; // true if the game is a draw
}