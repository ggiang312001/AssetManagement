import React from 'react';
import { useNavigate } from 'react-router-dom';
import { EditFilled, CloseCircleOutlined } from '@ant-design/icons';
import { Table, Modal, Space, message } from 'antd';
import { Button } from 'antd';
import { Select } from 'antd';
import {
	getUserList,
	getUserByStaffCode,
	disableUser,
} from '../../services/userService';
import { useState, useEffect } from 'react';
import CommonHeaderSort from '../../components/Common/CommonHeaderSort.jsx';
import { getAssignments } from '../../services/assignmentService';
import CommonSearch from '../../components/Common/CommonSearch';

const { Option } = Select;

const ManageUsers = () => {
	const navigate = useNavigate();

	function DateFormat(date) {
		let Day;
		if (new Date(date).getDate() < 10) {
			Day = '0' + new Date(date).getDate();
		} else {
			Day = new Date(date).getDate();
		}
		let Month;
		if (new Date(date).getMonth() < 10) {
			Month = '0' + (new Date(date).getMonth() + 1);
		} else {
			Month = new Date(date).getMonth() + 1;
		}
		const Year = new Date(date).getFullYear();
		const newDateFormat = `${Year}-${Month}-${Day}`;
		return newDateFormat;
	}

	const [dataSource, setDataSource] = useState([]);
	const [totalElements, setTotalElements] = useState(1);
	const pageSize = 10;
	const [loading, setLoading] = useState(false);
	const [detailUser, setDetailUser] = useState([]);
	const [isModalOpen, setIsModalOpen] = useState(false);
	const [sortBy, setSortBy] = useState('');
	function titleCase(str) {
		var splitStr = str.toLowerCase().split(' ');
		for (var i = 0; i < splitStr.length; i++) {
			// You do not need to check if i is larger than splitStr length, as your for does that for you
			// Assign it back to the array
			splitStr[i] =
				splitStr[i].charAt(0).toUpperCase() + splitStr[i].substring(1);
		}
		// Directly return the joined string
		return splitStr.join(' ');
	}
	const initialFilters = {
		searchTerm: '',
		pageSize: 10,
		type: null,
		pageNo: 1,
		sort: 'sort=updatedAt,desc',
	};
	const [filter, setFilter] = useState(initialFilters);

	const getData = React.useCallback(() => {
		setLoading(true);
		getUserList(filter)
			.then((res) => {
				const convertedData = res.data.content.map((data) => {
					const { staffCode, firstName, lastName, username, createdAt, role } =
						data;

					return {
						staffCode,
						fullName: firstName + ' ' + lastName,
						username,
						joinedDate: DateFormat(createdAt),
						type: titleCase(role),
					};
				});
				setDataSource(convertedData);
				setTotalElements(res.data.totalElements);
			})
			.catch((error) => {
				message.error(error.message);
			});
		setLoading(false);
	}, [filter]);

	// const [validAssignment, setValidAssignment] = useState(false);
	const [idDisableUser, setIdDisableUser] = useState('');

	const [canNotDisableUser, setCanNotDisableUser] = useState(false);
	const [canDisableUser, setCanDisableUser] = useState(false);

	const onSort = (dataIndex, sortDir) => {
		const newFilter = {
			...filter,
			sort: setSortFunction(dataIndex, sortDir),
			pageNo: 1,
		};
		setFilter(newFilter);
		setSortBy(dataIndex);
	};
	const setSortFunction = (dataIndex, sortDir) => {
		if (dataIndex === 'fullName') {
			return `sort=firstName,${sortDir}&sort=lastName,${sortDir}`;
		} else if (dataIndex === 'joinedDate') {
			return `sort=createdAt,${sortDir}`;
		} else if (dataIndex === 'type') {
			return `sort=role,${sortDir}`;
		} else if (dataIndex === 'staffCode') {
			return `sort=staffCode,${sortDir}`;
		} else {
			return `sort=username,${sortDir}`;
		}
	};
	const onClickCell = (record) => ({
		onClick: () => {
			getUserByStaffCode(record.staffCode)
				.then(({ data }) => {
					const {
						staffCode,
						username,
						firstName,
						lastName,
						birthDate,
						gender,
						locationId,
					} = data;

					setDetailUser([
						{
							label: 'Staff code',
							details: staffCode,
						},
						{
							label: 'Username',
							details: username,
						},
						{
							label: 'Fullname',
							details: `${firstName} ${lastName}`,
						},
						{
							label: 'Birthday',
							details: birthDate,
						},
						{
							label: 'Gender',
							details: gender,
						},
						{
							label: 'Location',
							details: locationId.city,
						},
					]);
				})
				.then(showModal);
		},
	});

	const columns = [
		{
			title: (
				<CommonHeaderSort
					title='Staff code'
					dataIndex='staffCode'
					onSort={onSort}
					sortBy={sortBy}
				/>
			),
			dataIndex: 'staffCode',
			key: 'staffCode',
			onCell: onClickCell,
		},
		{
			title: (
				<CommonHeaderSort
					title='Full name'
					dataIndex='fullName'
					onSort={onSort}
					sortBy={sortBy}
				/>
			),
			dataIndex: 'fullName',
			key: 'fullName',
			onCell: onClickCell,
		},
		{
			title: (
				<CommonHeaderSort
					title='Username'
					dataIndex='username'
					onSort={onSort}
					sortBy={sortBy}
				/>
			),
			dataIndex: 'username',
			key: 'username',
			onCell: onClickCell,
		},
		{
			title: (
				<CommonHeaderSort
					title='Joined Date'
					dataIndex='joinedDate'
					onSort={onSort}
					sortBy={sortBy}
				/>
			),
			dataIndex: 'joinedDate',
			key: 'joinedDate',
			onCell: onClickCell,
		},
		{
			title: (
				<CommonHeaderSort
					title='Type'
					dataIndex='type'
					onSort={onSort}
					sortBy={sortBy}
				/>
			),
			dataIndex: 'type',
			key: 'type',
			onCell: onClickCell,
		},
		{
			dataIndex: 'action',
			key: 'action',
			render: (_, record) => (
				<Space size='small'>
					<Button
						onClick={() =>
							navigate(`/manage-users/${record.staffCode}/edit-user`)
						}>
						<EditFilled />
					</Button>
					<Button
						onClick={() => {
							getAssignments({
								searchTerm: record.username,
								dateFill: '',
								stateFill: 'WAITING_FOR_ACCEPTANCE',
								pageSize: 1,
								pageNo: 1,
								sortBy: '',
								sortDir: '',
							}).then((res) => {
								if (res.data.totalElements !== 0) {
									showModalCanNotDisableUser();
								} else {
									getAssignments({
										searchTerm: record.username,
										dateFill: '',
										stateFill: 'ACCEPTED',
										pageSize: 1,
										pageNo: 1,
										sortBy: '',
										sortDir: '',
									}).then((response) => {
										if (response.data.totalElements !== 0) {
											showModalCanNotDisableUser();
										} else {
											showModalCanDisableUser();
											setIdDisableUser(record.staffCode);
										}
									});
									// showModalCanNotDisableUser();
								}
							});
						}}>
						<span style={{ color: 'red' }}>
							<CloseCircleOutlined />
						</span>
					</Button>
				</Space>
			),
		},
	];

	const detailColumns = [
		{
			title: 'Label',
			dataIndex: 'label',
			key: 'label',
		},
		{
			title: 'Details',
			dataIndex: 'details',
			key: 'details',
		},
	];

	const showModal = () => {
		setIsModalOpen(true);
	};

	const showModalCanNotDisableUser = () => {
		setCanNotDisableUser(true);
	};

	const showModalCanDisableUser = () => {
		setCanDisableUser(true);
	};

	const handleCancel = () => {
		setIsModalOpen(false);
		setCanNotDisableUser(false);
		setCanDisableUser(false);
	};

	const handleOKDisableUser = () => {
		disableUser(idDisableUser).then(() => {
			setCanDisableUser(false);
			const newFilter = {
				...filter,
				pageNo:
					totalElements % pageSize === 1 &&
					totalElements / pageSize + 1 === filter.pageNo + 0.1
						? filter.pageNo - 1
						: filter.pageNo,
			};
			setFilter(newFilter);
		});
		message.success('Disable user successfully!');
	};

	const fetchRecords = (page) => {
		const newFilter = {
			...filter,
			pageNo: page,
		};
		setFilter(newFilter);
	};
	useEffect(() => {
		getData();
	}, [getData]);
	return (
		<>
			<h1>Manage User</h1>
			<Space size='middle' style={{ margin: '20px 0' }}>
				<Select
					style={{ width: 200 }}
					placeholder='Type'
					onChange={(value) => {
						const newFilter = {
							...filter,
							type: value,
							pageNo: 1,
						};
						setFilter(newFilter);
					}}>
					<Option value={null}>All</Option>
					<Option value='ADMIN'>Admin</Option>
					<Option value='STAFF'>Staff</Option>
				</Select>
				<CommonSearch
					placeholder='Search'
					onSearch={(value) => {
						const newFilter = {
							...filter,
							searchTerm: value,
							pageNo: 1,
						};
						setFilter(newFilter);
					}}
					value={filter.searchTerm}
				/>
				<Button
					style={{ backgroundColor: 'red', color: 'white' }}
					danger
					onClick={() => navigate(`/manage-users/create-user/`)}>
					Create new user
				</Button>
			</Space>
			<Table
				loading={loading}
				dataSource={dataSource}
				columns={columns}
				pagination={{
					current: filter.pageNo,
					pageSize: pageSize,
					total: totalElements,
					onChange: (page) => {
						fetchRecords(page);
					},
					hideOnSinglePage: true,
				}}
			/>
			<Modal
				title='Detailed User Information'
				open={isModalOpen}
				onCancel={handleCancel}
				footer={null}>
				<Table
					columns={detailColumns}
					dataSource={detailUser}
					size='small'
					pagination={{
						hideOnSinglePage: true,
					}}
				/>
			</Modal>

			<Modal
				title='Can not disable user'
				open={canNotDisableUser}
				onCancel={handleCancel}
				footer={null}>
				There are valid assignments belonging to this user .<br />
				Please close all assignments before disabling user .
			</Modal>
			<Modal
				title='Are you sure ?'
				open={canDisableUser}
				onCancel={handleCancel}
				onOk={handleOKDisableUser}
				footer={null}>
				Do you want to delete this user ?<br />
				<br />
				<Button
					className='button-modal'
					type='danger'
					key='back'
					onClick={handleOKDisableUser}>
					Delete
				</Button>
				<Button
					className='button-modal'
					key='submit'
					type=''
					loading={loading}
					onClick={handleCancel}>
					Cancel
				</Button>
			</Modal>
		</>
	);
};
export default ManageUsers;
