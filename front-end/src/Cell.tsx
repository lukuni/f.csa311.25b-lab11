// BoardCell.tsx
import React from 'react';
import { Cell } from './game'; // Using the correct shared interface

interface Props {
  cell: Cell;
}

class BoardCell extends React.Component<Props> {
  render(): React.ReactNode {
    const { content, playable } = this.props.cell;
    const className = `cell${playable ? ' playable' : ''}`;

    return (
      <div className={className}>
        {content}
      </div>
    );
  }
}

export default BoardCell;
