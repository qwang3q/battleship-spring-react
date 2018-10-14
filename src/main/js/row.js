const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

import Cell from './cell.js'

export default class Row extends React.Component {
	render() {
        const row = this.props.cells.map(cell =>
            <Cell key={cell._links.self.href} cell={cell}/>
        );
		return (
			<tr>
				{row}
			</tr>
		)
	}
}
