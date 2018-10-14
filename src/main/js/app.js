// const React = require('react');
// const ReactDOM = require('react-dom');
// const client = require('./client');


// class App extends React.Component {

// 	constructor(props) {
// 		super(props);
//         this.state = { boards: [] };
//         this.loadFromServer();
// 		// this.loadBoards = this.loadBoards.bind(this);
// 		this.loadFromServer = this.loadFromServer.bind(this);
// 	}

//     loadFromServer() {
//         client({method: 'GET', path: '/boards'}).done(response => {
//             this.setState({boards: response.entity._embedded.boards});
//         });
//     }

// 	componentDidMount() {
// 		this.state.boards.forEach( board => {
//             if(board.type == 'userFleetBoard') {
//                 this.setState({ userFleetBoard: board });
//             } else {
//                 this.setState({ computerFleetBoard: board });
//             }
//         });
// 	}

// 	render() {
//         const sts = this.state.boards.map( board =>
//             <Board key={board._links.self.href} board={board} />
//         );
// 		return (
// 		    <div>
//                 {sts}
// 			</div>
// 		)
// 	}
// }


// class Board extends React.Component {
//     constructor(props) {
//         super(props);

//         this.state = {
//             cells : [],
//             rows  : []
//         };

//         this.updateState();

// //        this.updateState = this.updateState.bind(this);


//     }

//     updateState() {
//         client({method: 'GET', path: this.props.board._links.cells.href}).done(response => {
//             this.setState({cells: response.entity._embedded.cells});
//         });
//     }

//     componentDidMount() {
//         if(this.state.cells.length !== 0) {
//             var maps = [];
//             for(var r=0; r<this.props.board.mapHeight; r++) {
//                 maps.push(this.props.cells.slice(r*this.props.board.mapWidth, (r+1)*this.props.board.mapWidth));
//             }
//             this.setState({ rows: maps.map( cellsInRow =>
//                 <Row key={cellsInRow} cells={cellsInRow} />
//             ) });
//         }
//     }

// 	render() {
//         const foobar = "   ";

//         return (
//         <div className="game-board">
//             <div className="status">{this.props.board.type}</div>
//            {this.state.rows}
//            <div className="status">{foobar}</div>
//         </div>
//         )
//     }
// }

// class Row extends React.Component {
// 	render() {
//         const row = this.props.cells.map(cell =>
//             <Cell key={cell._links.self.href} cell={cell}/>
//         );
// 		return (
// 			<div className="board-row">
// 				{row}
// 			</div>
// 		)
// 	}
// }

// class Cell extends React.Component {
//     constructor(props) {
//         super(props);
//         this.state = {

//         };
//         this.handleHit = this.handleHit.bind(this);
//         this.updateState = this.updateState.bind(this);
//     }

//     updateState() {
//         client({method: 'GET', path: this.props.cell._links.self.href}).done(response => {
//             this.setState({cell: response.entity});
//             this.setState({value: this.props.cell.sunk == true ? "." : this.props.cell.hit == true ? "X" : this.props.cell.shipCell == true ? "S" : " "});
//         });
//     }

//     componentDidMount() {
//         this.updateState();
//     }

//     handleHit() {
//         client({method: 'GET', path: '/hitcell?href=' + this.props.cell._links.self.href }).done(response => {

//         });
//         this.updateState();
//     }

//     render() {
//         return (
//             <button
//                 className="square"
//                 onClick={this.handleHit}
//             >
//               {this.state.value}
//             </button>
//         )
//     }
// }


// ReactDOM.render(
// 	<App />,
// 	document.getElementById('react')
// )
