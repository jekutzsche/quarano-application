module.exports = {
  name: 'auth-change-password',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/change-password',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
