const React = require('react');
const ReactDOM = require('react-dom');
const client = require('./client');

import Row from './row.js'
import Cell from './cell.js'

export default class Board extends React.Component {
    constructor(props) {
        super(props);
        this.state = {cells : []};
    }

    componentDidMount() {
        client({method: 'GET', path: this.props.board._links.cells.href}).done(response => {
            this.setState({cells: response.entity._embedded.cells});
        });
    }

	render() {
//        const maps = this.state.cells.map((cell, i) => {
//            if(i % this.props.colCount == 0)
//                return (<tr><Cell key={cell._links.self.href} cell={cell} />)
//            if(i % this.props.colCount == this.props.colCount - 1)
//                return (<Cell key={cell._links.self.href} cell={cell} /></tr>)
//            return (<Cell key={cell._links.self.href} cell={cell} />)
//        });

//        var maps = [];
//        for(var r=0; r<this.props.rowCount; r++) {
//            var cellsInRow = [];
//            for(var c=0; c<this.props.colCount; c++) {
//                var i =  r*this.props.colCount + c;
//                cellsInRow.push(this.state.cells[i]);
//            }
//
//            maps.push(cellsInRow);
//        }
//
//        const board = maps.map(cellsInRow =>
//            <Row cells={cellsInRow} />
//        );

        const foobar =this.state.cells.map((cell, i) => {
              return (<Cell key={cell._links.self.href} cell={cell} />)
          });

        return (
        <table>
            <tbody>
            <tr>
           {foobar}
           </tr>
           </tbody>
        </table>
        )
    }
}
